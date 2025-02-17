package com.sudurukBackBack.Modu_Lecture.domain.lecture.exception;

import com.sudurukBackBack.Modu_Lecture.global.exception.BasicException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public final class LectureNotFoundException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String errorMessage() {
        return "강의를 찾을 수 없습니다.";
    }
}
