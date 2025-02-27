package com.sudurukBackBack.Modu_Lecture.domain.community.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "userId")
public class PostUpdateRequestDto {
    @NotNull(message = "아이디는 필수입력")
    private Long userId;

    private String newContent;
}
