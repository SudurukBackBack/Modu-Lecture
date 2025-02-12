package com.sudurukBackBack.Modu_Lecture.domain.user.dto.response;

public record UserDeleteResponseDto(
        String message
) {
    public static UserDeleteResponseDto of() {
        return new UserDeleteResponseDto("회원 탈퇴 처리되었습니다.");
    }
}
