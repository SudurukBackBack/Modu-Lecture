package com.sudurukBackBack.Modu_Lecture.domain.user.exception;

import com.sudurukBackBack.Modu_Lecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String errorMessage() {
        return "사용자를 찾을 수 없습니다.";
    }
}
