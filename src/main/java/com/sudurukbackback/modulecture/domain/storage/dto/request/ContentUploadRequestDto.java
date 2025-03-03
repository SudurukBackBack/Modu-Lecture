package com.sudurukbackback.modulecture.domain.storage.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ContentUploadRequestDto {
    private Long lectureId;
    private MultipartFile file;
}
