package com.sudurukbackback.modulecture.domain.user.dto.response;

public record UserLoginResponseDto(
        String message,
        String token
) {
    public static UserLoginResponseDto of(String token) {
        return new UserLoginResponseDto(
                "로그인이 완료되었습니다.",
                token
        );
    }
}
