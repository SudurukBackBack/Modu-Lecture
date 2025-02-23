package com.sudurukBackBack.Modu_Lecture.domain.community.exception;

import com.sudurukBackBack.Modu_Lecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends BasicException {
    @Override
    public int statusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String errorMessage() {
        return "댓글을 찾을 수 없습니다.";
    }
}
