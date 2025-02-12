package com.sudurukBackBack.Modu_Lecture.domain.user.controller;

import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.dto.response.UserDeleteResponseDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/profile")
@RestController
public class UserController {

    private final UserService userService;

    @DeleteMapping("/deactivate")
    public UserDeleteResponseDto deleteUser(
            @Valid @RequestBody UserDeleteRequestDto request,
            Authentication auth
    ) {
        userService.deleteUser(auth, request);

        return UserDeleteResponseDto.of();
    }
}
