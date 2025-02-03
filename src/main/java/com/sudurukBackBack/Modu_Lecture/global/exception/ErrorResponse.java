package com.sudurukBackBack.Modu_Lecture.global.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private int statusCode;
    private String errorMessage;
}
