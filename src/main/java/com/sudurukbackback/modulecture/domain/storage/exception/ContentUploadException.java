package com.sudurukbackback.modulecture.domain.storage.exception;


import com.sudurukbackback.modulecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

/**
 콘텐츠 생성 시 발생하는 예외를 나타냅니다.
 **/
public class ContentUploadException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String errorMessage() {
        return "콘텐츠 업로드 과정에서 문제가 발생하였습니다.";
    }
}