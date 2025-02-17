package com.sudurukBackBack.Modu_Lecture.domain.lecture.exception;

import org.springframework.http.HttpStatus;

public final class LectureNotFoundException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String errorMessage() {
        return "강의를 찾을 수 없습니다.";
    }
}
