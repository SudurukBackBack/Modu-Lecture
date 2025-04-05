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

    private static final String ACCESS_TOKEN = "access";
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

        var accessCookie = createCookie(ACCESS_TOKEN, token, 24);
        var refreshCookie = createCookie(REFRESH_TOKEN, refreshToken,48);

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
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

        var accessCookie = createCookie(ACCESS_TOKEN, "", 0);
        var refreshCookie = createCookie(REFRESH_TOKEN, "", 0);

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok().body(Map.of("message", "로그아웃 성공"));
    }

    // TODO: Spring Security 우선권으로 인해 토큰 갱신 기능 작동 안함
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

        var accessCookie = createCookie(ACCESS_TOKEN, newToken, 24);
        var refreshCookie = createCookie(REFRESH_TOKEN, newRefreshToken, 48);

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        redisTemplate.opsForValue().set("RT:" + email, newRefreshToken, 2, TimeUnit.DAYS);

        log.info("토큰 갱신 완료");

        return UserLoginResponseDto.of(token);
    }

    /**
     * 토큰을 쿠키에 저장
     *
     * @param cookieName 쿠키 종류
     * @param token 토큰
     * @param hours 만료 시간 (시간 단위)
     * @return ResponseCookie
     */
    private ResponseCookie createCookie(String cookieName, String token, int hours) {

        return ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofHours(hours)) // 만료
                .build();
    }
}
