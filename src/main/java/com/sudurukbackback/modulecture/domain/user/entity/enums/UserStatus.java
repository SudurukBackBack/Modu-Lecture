package com.sudurukbackback.modulecture.domain.user.entity.enums;

import lombok.Getter;

@Getter
public enum UserStatus {

    ACTIVE("활성 계정"),
    INACTIVE("휴면 계정"),
    RESTRICTED("이용 제한 계정"),
    BANNED("영구 정지 계정"),
    PENDING("삭제 대기 계정"),
    DELETED("삭제된 계정");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }
}
