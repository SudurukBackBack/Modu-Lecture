package com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureCreateRequestDto {
    @NotBlank(message = "강의 제목은 필수 입력값입니다.")
    @Size(min = 5, max = 100, message = "강의 제목은 최소 5자 이상, 최대 100자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "강의 설명은 필수 입력값입니다.")
    @Size(max = 1000, message = "강의 설명은 최대 1000자까지 가능합니다.")
    private String description;

    @NotBlank(message = "강사명은 필수 입력값입니다.")
    private String instructor;

    @NotBlank(message = "카테고리는 필수 입력값입니다.")
    private String category;

    @NotNull(message = "가격은 필수 입력값입니다.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    @Max(value = 1000000, message = "가격은 최대 1,000,000원까지 설정할 수 있습니다.")
    private Integer price;

    @NotNull(message = "강의 시간은 필수 입력값입니다.")
    @Min(value = 10, message = "강의 시간은 최소 10분 이상이어야 합니다.")
    @Max(value = 600, message = "강의 시간은 최대 600분까지 가능합니다.")
    private Integer duration;
}
