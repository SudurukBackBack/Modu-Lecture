package com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureCreateRequestDto {
    @NotBlank(message = "강의 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "강의 설명은 필수입니다.")
    private String description;

    @NotBlank(message = "강사명은 필수입니다.")
    private String instructor;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotNull(message = "가격은 필수입니다.")
    private Integer price;

    @NotNull(message = "강의 시간(분)은 필수입니다.")
    private Integer duration;
}
