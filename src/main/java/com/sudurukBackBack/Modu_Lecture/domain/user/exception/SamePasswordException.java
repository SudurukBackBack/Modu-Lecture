package com.sudurukBackBack.Modu_Lecture.domain.user.exception;

import com.sudurukBackBack.Modu_Lecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public final class SamePasswordException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String errorMessage() {
        return "새 비밀번호는 기존 비밀번호와 다르게 설정해야 합니다.";
    }
}
