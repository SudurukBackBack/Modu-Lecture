package com.sudurukbackback.modulecture.domain.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
public class PostUpdateRequestDto {
    @NotBlank(message = "공백으로 수정할 수 없습니다.")
    private String newContent;
}
