package com.sudurukbackback.modulecture.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    // BasicException 처리
    @org.springframework.web.bind.annotation.ExceptionHandler(BasicException.class)
    protected ResponseEntity<ErrorResponse> handleBasicException(BasicException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.statusCode())
                .errorMessage(e.errorMessage())
                .build();

        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    // @Valid 검증 예외 처리
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getBindingResult().getFieldErrors());

        // 필드별 에러 메시지를 맵으로 변환
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        // ErrorResponse 객체 생성
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage("입력한 정보가 올바르지 않습니다. 다시 확인해주세요.")
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    // 그 외 예외 처리 (Custom Exception이 구현되지 않은 예외 처리)
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleGlobalException(Exception e) {
        log.error("Unexpected error occurred: ", e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage("서버 내부 오류가 발생했습니다.")
                .build();

        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    // 권한이 없는 서비스에 접근
    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied: ", e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .errorMessage("접근할 수 없는 서비스 입니다. 관리자에 문의하세요.")
                .build();

        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }
}
