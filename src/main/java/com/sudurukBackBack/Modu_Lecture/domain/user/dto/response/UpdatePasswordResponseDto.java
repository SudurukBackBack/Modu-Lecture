package com.sudurukBackBack.Modu_Lecture.domain.user.dto.response;

public record UpdatePasswordResponseDto(
        String message
) {
    public static UpdatePasswordResponseDto of() {
        return new UpdatePasswordResponseDto("비밀번호 변경이 완료되었습니다.");
    }
}
