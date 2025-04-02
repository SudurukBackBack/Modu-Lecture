package com.sudurukbackback.modulecture.domain.user.controller;

import com.sudurukbackback.modulecture.domain.user.dto.request.UserLoginRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserRegistrationRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserLoginResponseDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserRegistrationResponseDto;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.service.AuthService;
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

    private static final String COOKIE_NAME = "jwtToken";

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
        var token = jwtTokenProvider.generateToken(user);

        ResponseCookie jwtCookie = ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)                 // JS 접근 불가능(XSS 방어)
                .secure(true)                   // HTTPS 에서만 쿠키 전송
                .sameSite("Strict")             // CSRF 방어
                .path("/")                      // 모든 경로에서 접근 가능
                .maxAge(Duration.ofHours(7))    // 7시간 유지
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return UserLoginResponseDto.of(token);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String token = JwtUtil.resolveToken(request);
        log.info("로그아웃 요청 들어옴 - token: {}", token);

        if (token != null) {
            log.info("Redis에 저장 시도 - key: BL:{}, value: logout", token);
            long expiration = JwtUtil.getExpiration(token);
            redisTemplate.opsForValue().set("BL:" + token, "logout", expiration, TimeUnit.MILLISECONDS);
        }

        ResponseCookie jwtCookie = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(0)) // 만료
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.ok().body(Map.of("message", "로그아웃 성공"));
    }
}
