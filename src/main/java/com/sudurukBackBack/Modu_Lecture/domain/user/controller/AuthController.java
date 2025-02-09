package com.sudurukBackBack.Modu_Lecture.domain.user.controller;

import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.PasswordUpdateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.UserLoginRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.UserRegistrationRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.dto.response.UserLoginResponseDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.dto.response.UserRegistrationResponseDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.entity.User;
import com.sudurukBackBack.Modu_Lecture.domain.user.service.AuthService;
import com.sudurukBackBack.Modu_Lecture.global.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

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
            @Valid @RequestBody UserLoginRequestDto request
    ) {
        var user = authService.authenticate(request);
        var token = jwtTokenProvider.generateToken(user);

        return UserLoginResponseDto.of(token);
    }

    @PutMapping("/password")
    public void updatePassword(
            @RequestBody PasswordUpdateRequestDto request,
            Authentication auth
    ) {
        authService.updatePassword(auth.getName(), request);
    }

}
