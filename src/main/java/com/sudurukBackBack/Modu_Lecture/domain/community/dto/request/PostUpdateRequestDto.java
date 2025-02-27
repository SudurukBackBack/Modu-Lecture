package com.sudurukBackBack.Modu_Lecture.domain.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "userId")
public class PostUpdateRequestDto {
    @NotNull(message = "회원 아이디는 필수입력")
    private Long userId;

    @NotBlank(message = "공백으로 수정할 수 없습니다.")
    private String newContent;
}
