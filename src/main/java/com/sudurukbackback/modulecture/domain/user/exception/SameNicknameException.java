package com.sudurukbackback.modulecture.domain.user.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public final class SameNicknameException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String errorMessage() {
        return "이미 사용 중인 닉네임 입니다.";
    }
}