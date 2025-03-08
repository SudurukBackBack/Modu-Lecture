package com.sudurukbackback.modulecture.domain.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateRequestDto {
    @NotNull(message = "아이디는 필수입력")
    private Long userId;

    @NotNull
    private Long postId;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
