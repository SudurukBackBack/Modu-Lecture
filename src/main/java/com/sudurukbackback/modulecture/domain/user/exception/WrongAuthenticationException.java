package com.sudurukbackback.modulecture.domain.user.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public final class WrongAuthenticationException extends BasicException {

    private final String message;
    private final int statusCode = HttpStatus.UNAUTHORIZED.value();

    public WrongAuthenticationException() {
        this.message = "이메일 또는 비밀번호가 올바르지 않습니다.\n5회 이상 실패할 시 30초 동안 로그인할 수 없습니다.";
    }

    public WrongAuthenticationException(int remainingAttempts) {
        this.message = String.format("이메일 또는 비밀번호가 올바르지 않습니다.\n%d회 남았습니다. (총 5회 허용).", remainingAttempts);
    }

    public WrongAuthenticationException(long remainingLockoutTime) {
        this.message = String.format("로그인 시도 횟수 초과. %d초 후에 다시 시도해주세요.", remainingLockoutTime);
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public String errorMessage() {
        return message;
    }
}
