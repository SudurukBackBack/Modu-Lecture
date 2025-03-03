package com.sudurukbackback.modulecture.domain.user.dto.response;

public record UserProfileResponseDto(
        String email,
        String nickname
) {
    public static UserProfileResponseDto of(String email, String nickname) {
        return new UserProfileResponseDto(
                email,
                nickname
        );
    }
}
