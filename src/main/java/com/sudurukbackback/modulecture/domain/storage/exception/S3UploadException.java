package com.sudurukbackback.modulecture.domain.storage.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

/**
 * S3 업로드 중 발생하는 예외를 나타냅니다.
 */
public class S3UploadException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String errorMessage() {
        return "S3 업로드 과정에서 문제가 발생하였습니다.";
    }
}