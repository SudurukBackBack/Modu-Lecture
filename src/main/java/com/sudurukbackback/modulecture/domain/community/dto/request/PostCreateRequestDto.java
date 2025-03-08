package com.sudurukbackback.modulecture.domain.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostCreateRequestDto {

    @NotBlank(message = "카테고리를 선택해주세요.")
    private String category;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 50, message = "제목은 최대 50자까지 입력 가능합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
