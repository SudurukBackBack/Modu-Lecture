package com.sudurukbackback.modulecture.domain.community.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
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
