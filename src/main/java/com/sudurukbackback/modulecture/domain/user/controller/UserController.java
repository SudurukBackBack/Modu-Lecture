package com.sudurukbackback.modulecture.domain.user.controller;

import com.sudurukbackback.modulecture.domain.user.dto.request.PasswordUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserProfileUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UpdatePasswordResponseDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserDeleteResponseDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserProfileResponseDto;
import com.sudurukbackback.modulecture.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @PatchMapping("/password")
    public UpdatePasswordResponseDto updatePassword(
            @Valid @RequestBody PasswordUpdateRequestDto request,
            Authentication auth
    ) {
        userService.updatePassword(auth, request);

        return UpdatePasswordResponseDto.of();
    }

    @PatchMapping("/deactivate")
    public UserDeleteResponseDto deleteUser(
            @Valid @RequestBody UserDeleteRequestDto request,
            Authentication auth
    ) {
        userService.deactivateAccount(auth, request);

        return UserDeleteResponseDto.of();
    }

    @GetMapping("/me")
    public UserProfileResponseDto getUserProfile(
            Authentication auth
    ) {
        return userService.getUserProfile(auth.getName());
    }

    @PatchMapping("/me")
    public UserProfileResponseDto updateUserProfile(
            @Valid @RequestBody UserProfileUpdateRequestDto request,
            Authentication auth
    ) {
        return userService.updateUserProfile(auth.getName(), request.getNickname());
    }
}
