package com.sudurukbackback.modulecture.domain.user.dto.response;

public record UserLoginResponseDto(
        String token
) {
    public static UserLoginResponseDto of(String token) {
        return new UserLoginResponseDto(token);
    }
}
