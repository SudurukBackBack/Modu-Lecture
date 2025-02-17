package com.sudurukBackBack.Modu_Lecture.domain.user.dto.response;

public record UserLoginResponseDto(
        String token
) {
    public static UserLoginResponseDto of(String token) {
        return new UserLoginResponseDto(token);
    }
}
