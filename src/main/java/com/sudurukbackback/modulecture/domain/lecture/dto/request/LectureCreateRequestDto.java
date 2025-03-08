package com.sudurukbackback.modulecture.domain.lecture.dto.request;

import com.sudurukbackback.modulecture.domain.lecture.entity.LectureStatus; //  LectureStatus 추가
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class LectureCreateRequestDto {

    @NotBlank(message = "강의 제목은 필수 입력값입니다.")
    @Size(min = 5, max = 100, message = "강의 제목은 최소 5자 이상, 최대 100자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "강의 설명은 필수 입력값입니다.")
    private String description;

    @NotEmpty(message = "카테고리는 필수 입력값입니다.")
    private List<Long> categoryIds;

    @NotNull(message = "가격은 필수 입력값입니다.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    @Max(value = 1000000, message = "가격은 최대 1,000,000원까지 설정할 수 있습니다.")
    private Integer price;

    @NotNull(message = "재생시간은 필수 입력값입니다.")
    private String duration;

    @NotNull(message = "강의 영상은 필수입니다.")
    private MultipartFile video;
}