package com.sudurukBackBack.Modu_Lecture.domain.lecture.entity;

import lombok.Getter;

@Getter
public enum LectureStatus {
    ACTIVE(1),    // 활성화 (강의 공개)
    INACTIVE(0),  // 비활성화 (비공개)
    BANNED(-1);   // 금지 (관리자 삭제 등)

    private final int code;

    LectureStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
