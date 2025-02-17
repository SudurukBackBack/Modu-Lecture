package com.sudurukBackBack.Modu_Lecture.domain.storage.exception;

/**
 * 파일 저장 중 발생하는 예외를 나타냅니다.
 */
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}