package com.sudurukBackBack.Modu_Lecture.domain.storage.exception;

import com.sudurukBackBack.Modu_Lecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

/**
 * 콘텐츠 처리 중 발생하는 예외를 나타냅니다.
 */
public final class ContentProcessingException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String errorMessage() {
        return "콘텐츠 업로드 중 오류가 발생하였습니다. (재생시간 추출, 썸네일 생성 등)";
    }
}