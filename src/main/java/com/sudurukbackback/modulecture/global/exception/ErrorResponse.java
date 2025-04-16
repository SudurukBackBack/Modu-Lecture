package com.sudurukbackback.modulecture.global.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ErrorResponse {

    private int statusCode;

    private String errorMessage;

    private final Map<String, String> validationErrors;
}
