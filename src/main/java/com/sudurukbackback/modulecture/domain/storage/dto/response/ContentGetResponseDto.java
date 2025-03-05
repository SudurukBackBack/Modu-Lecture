package com.sudurukbackback.modulecture.domain.storage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentGetResponseDto {
    private String videoUrl;
    private String imageUrl;
}