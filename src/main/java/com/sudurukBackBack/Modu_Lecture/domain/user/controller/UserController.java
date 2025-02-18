package com.sudurukBackBack.Modu_Lecture.domain.user.controller;

import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.PasswordUpdateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.dto.response.UpdatePasswordResponseDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.dto.response.UserDeleteResponseDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PutMapping("/password")
    public UpdatePasswordResponseDto updatePassword(
            @Valid @RequestBody PasswordUpdateRequestDto request,
            Authentication auth
    ) {
        userService.updatePassword(auth, request);

        return UpdatePasswordResponseDto.of();
    }

    @DeleteMapping("/deactivate")
    public UserDeleteResponseDto deleteUser(
            @Valid @RequestBody UserDeleteRequestDto request,
            Authentication auth
    ) {
        userService.deactivateAccount(auth, request);

        return UserDeleteResponseDto.of();
    }
}
