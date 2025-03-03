package com.sudurukbackback.modulecture.domain.storage.dto.request;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class UploadContentRequestDto {
    private Long lectureId;
    private MultipartFile file;
}
