package com.sudurukBackBack.Modu_Lecture.domain.storage.controller;

import com.sudurukBackBack.Modu_Lecture.domain.storage.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
public class ContentController {

    private final S3UploadService s3UploadService;

    

}
