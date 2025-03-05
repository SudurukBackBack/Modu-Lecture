package com.sudurukbackback.modulecture.domain.lecture.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDto {

    private String message;
    private List<String> errors;

}
