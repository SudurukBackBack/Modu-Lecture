package com.sudurukBackBack.Modu_Lecture.domain.community.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequestDto {
    @NotNull(message = "아이디는 필수입력")
    private Long userId;

    private String newContent;
}
