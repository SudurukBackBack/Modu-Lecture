package com.sudurukBackBack.Modu_Lecture.domain.storage.exception;

/**
 * 콘텐츠 처리 중 발생하는 예외를 나타냅니다.
 */
public class ContentProcessingException extends RuntimeException {

    public ContentProcessingException(String message) {
        super(message);
    }

    public ContentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}