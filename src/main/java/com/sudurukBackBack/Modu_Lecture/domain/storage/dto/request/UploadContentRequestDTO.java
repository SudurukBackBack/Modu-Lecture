package com.sudurukBackBack.Modu_Lecture.domain.storage.dto.request;

import lombok.Data;

@Data
public class UploadContentRequestDTO {
    private Integer lectureId;
    private Integer categoryId;
    private String videoUrl;
}
