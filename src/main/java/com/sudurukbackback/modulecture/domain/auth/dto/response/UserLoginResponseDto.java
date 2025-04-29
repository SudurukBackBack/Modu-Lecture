package com.sudurukbackback.modulecture.domain.auth.dto.response;

public record UserLoginResponseDto(
        String message,
        String token
) {
    public static UserLoginResponseDto of(String token) {
        return new UserLoginResponseDto(
                "요청 처리 완료",
                token
        );
    }
}
