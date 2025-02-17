package com.sudurukBackBack.Modu_Lecture.domain.storage.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateContentRequestDTO {
    private Integer lectureId;
    private Integer categoryId;
    private String videoUrl;
}
