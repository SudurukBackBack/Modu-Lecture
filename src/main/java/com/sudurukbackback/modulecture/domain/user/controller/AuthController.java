package com.sudurukbackback.modulecture.domain.user.controller;

import com.sudurukbackback.modulecture.domain.user.dto.request.UserLoginRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserRegistrationRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserLoginResponseDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserRegistrationResponseDto;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.service.AuthService;
import com.sudurukbackback.modulecture.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private static final String COOKIE_NAME = "jwtToken";

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(
            HttpServletResponse response
    ) {
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
