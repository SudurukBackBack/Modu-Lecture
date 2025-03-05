package com.sudurukbackback.modulecture.domain.storage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentGetResponseDto {
    private Integer duration; // RDS의 content 테이블에서 조회해온 재생시간 정보
    private String videoUrl; // S3로부터 받아온 Presigned Url(강의 영상 Url 유출 방지)
    private String imageUrl; // S3로부터 받아온 Url
}