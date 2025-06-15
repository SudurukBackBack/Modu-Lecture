package com.sudurukbackback.modulecture.domain.user.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public class SocialUserNotAllowException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.FORBIDDEN.value();
    }

    @Override
    public String errorMessage() {
        return "소셜계정으로 로그인한 사용자는 사용할 수 없는 기능입니다.";
    }
}
