package com.sudurukBackBack.Modu_Lecture.domain.storage.controller;

import com.sudurukBackBack.Modu_Lecture.domain.storage.service.FFmpegService;
import com.sudurukBackBack.Modu_Lecture.domain.storage.service.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final S3FileUploadService s3UploadService;
    private final FFmpegService ffmpegService;

    @Value("${aws.s3.video-prefix}")
    private String videoPrefix;

    @Value("${aws.s3.image-prefix}")
    private String imagePrefix;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            // 동영상 파일을 임시 디렉토리에 저장
            String tempDir = System.getProperty("java.io.tmpdir");
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String savedFileName = UUID.randomUUID() + fileExtension;
            File videoFile = new File(tempDir, savedFileName);
            file.transferTo(videoFile);

            // S3에 동영상 업로드
            String videoS3Key = videoPrefix + savedFileName;
            s3UploadService.uploadFile(videoFile, videoS3Key);

            // FFmpegService 호출하여 이미지 추출 및 업로드
            ffmpegService.extractFramesAndUploadToS3(videoFile.getAbsolutePath(), videoS3Key);

            // 동영상 및 이미지의 S3 URL을 생성
            String videoUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, videoS3Key);

            String imageFileName = savedFileName.replace(".mp4", ".png");
            String imageS3Key = imagePrefix + imageFileName;
            String imageUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, imageS3Key);

            // TODO: 동영상 및 이미지 URL을 데이터베이스에 저장

            // 임시 파일 삭제
            videoFile.delete();

            return ResponseEntity.ok("동영상 및 이미지 업로드가 완료되었습니다.");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 업로드 중 오류가 발생하였습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("처리 중 오류가 발생하였습니다.");
        }
    }


}