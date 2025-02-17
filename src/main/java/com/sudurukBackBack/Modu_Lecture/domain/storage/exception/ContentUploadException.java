package com.sudurukBackBack.Modu_Lecture.domain.storage.exception;


/**
 콘텐츠 생성 시 발생하는 예외를 나타냅니다.
 **/
public class ContentUploadException extends RuntimeException {

    public ContentUploadException(String message) {
        super(message);
    }

    public ContentUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}