package com.sudurukBackBack.Modu_Lecture.domain.user.exception;

import com.sudurukBackBack.Modu_Lecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public final class UserAlreadyExistsException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.CONFLICT.value();
    }

    @Override
    public String errorMessage() {
        return "이미 존재하는 사용자 입니다.";
    }
}
