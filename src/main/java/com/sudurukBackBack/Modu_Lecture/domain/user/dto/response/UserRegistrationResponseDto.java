package com.sudurukBackBack.Modu_Lecture.domain.user.dto.response;

import com.sudurukBackBack.Modu_Lecture.domain.user.entity.User;

public record UserRegistrationResponseDto(
        String email,
        String username
) {
    public static UserRegistrationResponseDto of(User user) {
        return new UserRegistrationResponseDto(
                user.getEmail(),
                user.getUsername());
    }
}
