package com.sudurukbackback.modulecture.domain.auth.controller;

import com.sudurukbackback.modulecture.domain.auth.dto.request.UserLoginRequestDto;
import com.sudurukbackback.modulecture.domain.auth.dto.request.UserRegistrationRequestDto;
import com.sudurukbackback.modulecture.domain.auth.dto.response.CookieResultDto;
import com.sudurukbackback.modulecture.domain.auth.dto.response.UserRegistrationResponseDto;
import com.sudurukbackback.modulecture.domain.auth.service.AuthService;
import com.sudurukbackback.modulecture.domain.auth.service.CookieService;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/sign-up")
    public UserRegistrationResponseDto signUp(
            @Valid @RequestBody UserRegistrationRequestDto request
    ) {
        User user = authService.signUp(request);

        return UserRegistrationResponseDto.of(user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @Valid @RequestBody UserLoginRequestDto request,
            HttpServletResponse response
    ) {
        CookieResultDto cookies = authService.signIn(request);

        cookieService.setCookiesInHttpHeader(response, cookies.getAccessCookie(), cookies.getRefreshCookie());

        return ResponseEntity.ok("login success");
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        CookieResultDto cookies = authService.logout(request);

        cookieService.setCookiesInHttpHeader(response, cookies.getAccessCookie(), cookies.getRefreshCookie());

        return ResponseEntity.ok().body("logout success");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        CookieResultDto cookies = authService.refreshToken(request);

        cookieService.setCookiesInHttpHeader(response, cookies.getAccessCookie(), cookies.getRefreshCookie());

        log.info("토큰 갱신 완료");

        return ResponseEntity.ok().body("refresh success");
    }
}
