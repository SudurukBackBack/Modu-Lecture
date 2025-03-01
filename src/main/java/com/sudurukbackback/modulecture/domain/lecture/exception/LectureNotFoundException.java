package com.sudurukbackback.modulecture.domain.lecture.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LectureNotFoundException extends RuntimeException {
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public LectureNotFoundException(String message) {
        super(message);
    }
}