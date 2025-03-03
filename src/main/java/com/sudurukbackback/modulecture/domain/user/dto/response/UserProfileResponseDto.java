package com.sudurukbackback.modulecture.domain.user.dto.response;

import com.sudurukbackback.modulecture.domain.user.entity.User;

public record UserProfileResponseDto(
        String email,
        String nickname,
        String password
) {
    public static UserProfileResponseDto of(User user) {
        return new UserProfileResponseDto(
                user.getEmail(),
                user.getNickname(),
                "********"
        );
    }
}
