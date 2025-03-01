package com.sudurukbackback.modulecture.domain.lecture.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureUpdateRequestDto {

    @Size(min = 5, max = 100, message = "강의 제목은 최소 5자 이상, 최대 100자 이하여야 합니다.")
    private String title;

    @Size(max = 1000, message = "강의 설명은 최대 1000자까지 가능합니다.")
    private String description;

    private List<Long> categoryIds; // List<Long> categoryIds 추가

    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    @Max(value = 1000000, message = "가격은 최대 1,000,000원까지 설정할 수 있습니다.")
    private Integer price;

    @Min(value = 10, message = "강의 시간은 최소 10분 이상이어야 합니다.")
    @Max(value = 600, message = "강의 시간은 최대 600분까지 가능합니다.")
    private Integer duration;

    private MultipartFile video; // 강의 영상 변경 가능
}