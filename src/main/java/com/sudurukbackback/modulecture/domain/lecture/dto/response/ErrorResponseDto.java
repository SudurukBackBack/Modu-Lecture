package com.sudurukbackback.modulecture.domain.lecture.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorResponseDto {

    private String message;
    private List<String> errors;

    public ErrorResponseDto(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }

}
