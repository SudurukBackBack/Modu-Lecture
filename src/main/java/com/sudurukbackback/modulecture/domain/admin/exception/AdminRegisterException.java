package com.sudurukbackback.modulecture.domain.admin.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public class AdminRegisterException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.FORBIDDEN.value();
    }

    @Override
    public String errorMessage() {
        return "요청이 거부되었습니다.";
    }
}
