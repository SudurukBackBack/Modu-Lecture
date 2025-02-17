package com.sudurukBackBack.Modu_Lecture.domain.user.exception;

import com.sudurukBackBack.Modu_Lecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public final class AccountNotActiveException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.FORBIDDEN.value();
    }

    @Override
    public String errorMessage() {
        return "계정이 활성화되지 않았습니다. 관리자에게 문의하세요.";
    }
}
