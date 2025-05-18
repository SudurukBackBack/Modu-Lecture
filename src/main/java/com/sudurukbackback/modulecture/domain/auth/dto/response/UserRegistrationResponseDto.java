package com.sudurukbackback.modulecture.domain.auth.dto.response;

import com.sudurukbackback.modulecture.domain.user.entity.User;

public record UserRegistrationResponseDto(
        String email,
        String nickname
) {
    public static UserRegistrationResponseDto of(User user) {
        return new UserRegistrationResponseDto(
                user.getEmail(),
                user.getNickname());
    }
}
