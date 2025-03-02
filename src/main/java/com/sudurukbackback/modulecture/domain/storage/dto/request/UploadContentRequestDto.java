package com.sudurukbackback.modulecture.domain.storage.dto.request;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class UploadContentRequestDto {
    private Long lectureId;
    private List<Long> categoryIds;
    private String videoUrl;
}
