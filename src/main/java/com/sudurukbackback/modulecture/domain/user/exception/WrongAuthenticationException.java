package com.sudurukbackback.modulecture.domain.user.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public final class WrongAuthenticationException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }

    @Override
    public String errorMessage() {
        return "이메일 또는 비밀번호가 올바르지 않습니다.";
    }
}
