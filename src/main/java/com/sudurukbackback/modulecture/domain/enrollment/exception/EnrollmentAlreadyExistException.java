package com.sudurukbackback.modulecture.domain.enrollment.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public class EnrollmentAlreadyExistException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.CONFLICT.value();
    }

    @Override
    public String errorMessage() {
        return "이미 등록된 강의입니다.";
    }
}
