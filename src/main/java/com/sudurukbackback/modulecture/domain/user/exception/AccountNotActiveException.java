package com.sudurukbackback.modulecture.domain.user.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public final class AccountNotActiveException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.FORBIDDEN.value();
    }

    @Override
    public String errorMessage() {
        return "사용할 수 없는 계정입니다. 관리자에게 문의하세요.";
    }
}
