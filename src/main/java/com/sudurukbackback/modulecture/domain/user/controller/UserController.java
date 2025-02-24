package com.sudurukbackback.modulecture.domain.user.controller;

import com.sudurukbackback.modulecture.domain.user.dto.request.PasswordUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UpdatePasswordResponseDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserDeleteResponseDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserProfileResponseDto;
import com.sudurukbackback.modulecture.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/deactivate")
    public UserDeleteResponseDto deleteUser(
            @Valid @RequestBody UserDeleteRequestDto request,
            Authentication auth
    ) {
        userService.deactivateAccount(auth, request);

        return UserDeleteResponseDto.of();
    }

    @GetMapping("/me")
    public UserProfileResponseDto getUserInfo(
            Authentication auth
    ) {
        return userService.getUserProfile(auth.getName());
    }
}
