package com.sudurukBackBack.Modu_Lecture.domain.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequestDto {
    @NotNull(message = "아이디는 필수입력")
    private Long userId;

    @NotNull
    private Long postId;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
