package com.sudurukbackback.modulecture.global.exception;

import org.springframework.http.HttpStatus;

public final class BasicServerException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String errorMessage() {
        return "서버에 오류가 발생했습니다. 다시 시도해주세요.";
    }
}
