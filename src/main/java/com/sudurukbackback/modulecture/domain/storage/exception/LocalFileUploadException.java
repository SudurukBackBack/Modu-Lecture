package com.sudurukBackBack.Modu_Lecture.domain.storage.exception;

import com.sudurukBackBack.Modu_Lecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

/**
 * 파일 저장 중 발생하는 예외를 나타냅니다.
 */
public class LocalFileUploadException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String errorMessage() {
        return "로컬 파일 업로드 과정에서 문제가 발생하였습니다.";
    }
}