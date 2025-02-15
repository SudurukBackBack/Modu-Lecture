package com.sudurukBackBack.Modu_Lecture.domain.storage.exception;

public class S3FileUploadException extends RuntimeException {
    public S3FileUploadException(String message) {
        super(message);
    }

    public S3FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}