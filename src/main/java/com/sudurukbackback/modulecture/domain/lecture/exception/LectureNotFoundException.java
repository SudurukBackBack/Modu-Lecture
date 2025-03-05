package com.sudurukbackback.modulecture.domain.lecture.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LectureNotFoundException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String errorMessage() {
        return "해당 강의를 찾을 수 없습니다.";
    }
}