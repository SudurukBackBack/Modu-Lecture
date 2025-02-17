package com.sudurukBackBack.Modu_Lecture.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    // BasicException 처리
    @org.springframework.web.bind.annotation.ExceptionHandler(BasicException.class)
    protected ResponseEntity<?> handleBasicException(BasicException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.statusCode())
                .errorMessage(e.errorMessage())
                .build();

        return new ResponseEntity<>(errorResponse, Objects.requireNonNull(HttpStatus.resolve(e.statusCode())));
    }

    // @Valid 검증 예외 처리
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getBindingResult().getFieldErrors());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    // 그 외 예외 처리 (Custom Exception이 구현되지 않은 예외 처리)
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleGlobalException(Exception e) {
        log.error("Unexpected error occurred: ", e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage("서버 내부 오류가 발생했습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
