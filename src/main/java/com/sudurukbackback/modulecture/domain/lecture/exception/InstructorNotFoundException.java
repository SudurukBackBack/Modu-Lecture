package com.sudurukbackback.modulecture.domain.lecture.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InstructorNotFoundException extends RuntimeException {
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public InstructorNotFoundException() {
        super("해당 강사를 찾을 수 없습니다.");
    }
}