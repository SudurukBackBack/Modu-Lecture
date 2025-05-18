package com.sudurukbackback.modulecture.domain.user.controller;

import com.sudurukbackback.modulecture.domain.user.dto.request.PasswordUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserProfileUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserProfileResponseDto;
import com.sudurukbackback.modulecture.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> updatePassword(
            @Valid @RequestBody PasswordUpdateRequestDto request,
            Authentication auth
    ) {
        userService.updatePassword(auth, request);

        return ResponseEntity.ok("비밀번호 변경이 완료되었습니다.\n다시 로그인 해주세요.");
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<?> deleteUser(
            @Valid @RequestBody UserDeleteRequestDto request,
            Authentication auth
    ) {
        userService.deactivateAccount(auth, request);

        return ResponseEntity.ok("탈퇴가 완료되었습니다.") ;
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
