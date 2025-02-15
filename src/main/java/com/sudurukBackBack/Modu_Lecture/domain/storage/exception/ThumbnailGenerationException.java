package com.sudurukBackBack.Modu_Lecture.domain.storage.exception;


public class ThumbnailGenerationException extends RuntimeException {
    public ThumbnailGenerationException(String message) {
        super(message);
    }

    public ThumbnailGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}