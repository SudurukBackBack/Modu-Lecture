package com.sudurukbackback.modulecture.domain.storage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentGetResponseDto {
    private Integer lectureId;
    private Integer categoryId;
    private String videoUrl;
    private String imageUrl;
    private LocalDateTime createdAt;
}