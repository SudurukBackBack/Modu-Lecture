package com.sudurukBackBack.Modu_Lecture.domain.storage.exception;

import com.sudurukBackBack.Modu_Lecture.global.exception.BasicException;
import org.springframework.http.HttpStatus;

public class LocalFileDeleteException extends BasicException {

    @Override
    public int statusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value(); // HttpStatus를 임포트해야 합니다.
    }

    @Override
    public String errorMessage() {
        return "로컬 파일 삭제에 실패하였습니다.";
    }
}
