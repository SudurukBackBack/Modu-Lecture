package com.sudurukbackback.modulecture.domain.user.controller;

import com.sudurukbackback.modulecture.domain.user.dto.request.UserLoginRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserRegistrationRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserLoginResponseDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserRegistrationResponseDto;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.service.AuthService;
import com.sudurukbackback.modulecture.global.exception.BasicServerException;
import com.sudurukbackback.modulecture.global.security.JwtTokenProvider;
import com.sudurukbackback.modulecture.global.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private static final String ACCESS_TOKEN = "jwtToken";
    private static final String REFRESH_TOKEN = "refresh";

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    @PostMapping("/sign-up")
    public UserRegistrationResponseDto signUp(
            @Valid @RequestBody UserRegistrationRequestDto request
    ) {
        User user = authService.signUp(request);

        return UserRegistrationResponseDto.of(user);
    }

    @PostMapping("/sign-in")
    public UserLoginResponseDto signIn(
            @Valid @RequestBody UserLoginRequestDto request,
            HttpServletResponse response
    ) {
        var user = authService.signIn(request);
        var tokens = jwtTokenProvider.generateToken(user.getEmail());
        String token = tokens.get("access_token");
        String refreshToken = tokens.get("refresh_token");

        var jwtCookie = createAccessCookie(token, 24);
        var refreshCookie = createRefreshCookie(refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        redisTemplate.opsForValue().set("RT:" + user.getEmail(), refreshToken, 2, TimeUnit.DAYS);

        return UserLoginResponseDto.of(token);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String token = JwtUtil.resolveToken(request);
        log.info("로그아웃 요청 들어옴 - token: {}", token);

        if (token == null) {
            throw new BasicServerException();
        }

        long expiration = JwtUtil.getExpiration(token);
        redisTemplate.opsForValue().set("BL:" + token, "logout", expiration, TimeUnit.MILLISECONDS);
        redisTemplate.delete("RT:" + JwtUtil.getUsername(token));

        var jwtCookie = createAccessCookie("", 0);

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.ok().body(Map.of("message", "로그아웃 성공"));
    }

    @PostMapping("/refresh")
    public UserLoginResponseDto refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = JwtUtil.resolveToken(request);
        log.info("토큰 갱신 요청 처리 시작");

        if (!JwtUtil.validateToken(refreshToken)) {
            log.error("토큰 검증 실패");
            throw new BasicServerException();
        }

        String email = JwtUtil.getUsername(refreshToken);
        String token = redisTemplate.opsForValue().get("RT:" + email);

        if (!refreshToken.equals(token)) {
            log.error("토큰 값 불일치로 인한 토큰 갱신 작업 중단");
            throw new BasicServerException();
        }

        var newTokens = jwtTokenProvider.generateToken(email);
        String newToken = newTokens.get("access_token");
        String newRefreshToken = newTokens.get("refresh_token");

        var jwtCookie = createAccessCookie(newToken, 24);
        var refreshCookie = createRefreshCookie(newRefreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        redisTemplate.opsForValue().set("RT:" + email, newRefreshToken, 2, TimeUnit.DAYS);

        log.info("토큰 갱신 완료");

        return UserLoginResponseDto.of(token);
    }

    /**
     * access 토큰을 쿠키에 저장
     *
     * @param token 쿠키에 저장할 토큰 값
     * @param hours 쿠키 유효 시간 (hour 단위)
     * @return ResponseCookie
     */
    private ResponseCookie createAccessCookie(String token, int hours) {

        return ResponseCookie.from(ACCESS_TOKEN, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofHours(hours)) // 만료
                .build();
    }

    /**
     * refresh 토큰을 쿠키에 저장
     *
     * @param token 쿠키에 저장할 토큰 값
     * @return ResponseCookie
     */
    private ResponseCookie createRefreshCookie(String token) {

        return ResponseCookie.from(REFRESH_TOKEN, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(2))
                .build();
    }
}
