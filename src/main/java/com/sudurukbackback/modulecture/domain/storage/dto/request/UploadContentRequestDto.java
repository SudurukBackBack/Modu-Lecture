package com.sudurukbackback.modulecture.domain.storage.dto.request;

import lombok.Data;

@Data
public class UploadContentRequestDto {
    private Integer lectureId;
    private Integer categoryId;
    private String videoUrl;
}
