package com.sudurukbackback.modulecture.domain.community.exception;

import com.sudurukbackback.modulecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends BasicException {
    @Override
    public int statusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String errorMessage() {
        return "게시글을 찾을 수 없습니다.";
    }
}
