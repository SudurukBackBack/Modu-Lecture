package com.sudurukbackback.modulecture.domain.admin.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public class NoUpdateRequestException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String errorMessage() {
        return "수정할 정보가 없습니다.";
    }
}
