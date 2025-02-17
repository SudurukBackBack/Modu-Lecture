package com.sudurukBackBack.Modu_Lecture.domain.storage.exception;

/**
 * S3 업로드 중 발생하는 예외를 나타냅니다.
 */
public class S3UploadException extends RuntimeException {

    public S3UploadException(String message) {
        super(message);
    }

    public S3UploadException(String message, Throwable cause) {
        super(message, cause);
    }
}