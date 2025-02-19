package com.sudurukBackBack.Modu_Lecture.domain.storage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UploadContentResponseDto {
    private Integer lectureId;
    private Integer categoryId;
    private String videoUrl;
    private String imageUrl;
    private LocalDateTime createdAt;
}