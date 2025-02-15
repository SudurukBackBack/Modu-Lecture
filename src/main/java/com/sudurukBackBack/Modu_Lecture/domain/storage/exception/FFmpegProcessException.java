package com.sudurukBackBack.Modu_Lecture.domain.storage.exception;

public class FFmpegProcessException extends RuntimeException {
    // FFmpeg 실행 중 예상치 못한 예외가 발생했을 때
    public FFmpegProcessException(String message) {
        super(message);
    }

    public FFmpegProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}