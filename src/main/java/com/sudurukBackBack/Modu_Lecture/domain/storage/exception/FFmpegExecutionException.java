package com.sudurukBackBack.Modu_Lecture.domain.storage.exception;

public class FFmpegExecutionException extends RuntimeException {
    // FFmpeg 프로세스가 비정상 종료되었을 때
    // 실행은 정상이지만 결과 오류 발생 시
    public FFmpegExecutionException(String message) {
        super(message);
    }

    public FFmpegExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}