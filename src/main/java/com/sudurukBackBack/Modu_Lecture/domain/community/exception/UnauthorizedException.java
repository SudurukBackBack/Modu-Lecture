package com.sudurukBackBack.Modu_Lecture.domain.community.exception;

import com.sudurukBackBack.Modu_Lecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BasicException {
    @Override
    public int statusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }

    @Override
    public String errorMessage() {
        return "접근 권한이 없습니다.";
    }
}
