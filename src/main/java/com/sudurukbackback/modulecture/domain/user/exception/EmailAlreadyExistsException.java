package com.sudurukBackBack.Modu_Lecture.domain.user.exception;

import com.sudurukBackBack.Modu_Lecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public final class EmailAlreadyExistsException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.CONFLICT.value();
    }

    @Override
    public String errorMessage() {
        return "이미 사용 중인 이메일 입니다.";
    }
}
