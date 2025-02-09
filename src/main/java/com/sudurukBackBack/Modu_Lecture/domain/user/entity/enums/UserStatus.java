package com.sudurukBackBack.Modu_Lecture.domain.user.entity.enums;

import lombok.Getter;

@Getter
public enum UserStatus {

    ACTIVE(1, "활성 계정"),
    INACTIVE(0, "휴면 계정"),
    RESTRICTED(-1, "이용 제한 계정"),
    BANNED(-2, "영구 정지 계정"),
    DELETED(-99, "삭제된 계정");

    private final int code;
    private final String description;

    UserStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
