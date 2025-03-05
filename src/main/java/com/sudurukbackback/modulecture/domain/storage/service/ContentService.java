package com.sudurukbackback.modulecture.domain.storage.service;

import com.sudurukbackback.modulecture.domain.storage.entity.Content;
import com.sudurukbackback.modulecture.domain.storage.exception.ContentProcessingException;
import com.sudurukbackback.modulecture.domain.storage.repository.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final FileService fileService;
    private final ContentInfoService contentInfoService;
    private final S3UploadService s3UploadService;

    /**
     * 콘텐츠를 업로드하고 처리합니다.
     *
     * @param videoFile   콘텐츠 생성 요청 DTO
     */
    @Transactional
    public void uploadContent(Long lectureId, MultipartFile videoFile) {

        try {
            // 1. 비디오 파일을 로컬에 저장
            String localVideoPath = fileService.saveVideoFileLocally(videoFile);

            // 2. 동영상 재생 시간 추출
            int duration = contentInfoService.getVideoDurationInSeconds(localVideoPath);

            // 3. 썸네일 이미지 생성
            String thumbnailPath = contentInfoService.generateThumbnail(localVideoPath);

            // 4. 동영상 파일을 S3에 업로드
            String videoS3Key = "video/" + UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
            String videoUrl = s3UploadService.uploadFileToS3(localVideoPath, videoS3Key);

            // 5. 썸네일 이미지를 S3에 업로드
            String imageS3Key = "image/" + UUID.randomUUID() + "_" + Paths.get(thumbnailPath).getFileName();
            String imageUrl = s3UploadService.uploadFileToS3(thumbnailPath, imageS3Key);

            // 6. 로컬 파일 삭제
            fileService.deleteLocalFile(localVideoPath);
            fileService.deleteLocalFile(thumbnailPath);

            // 7. Content 엔티티 생성
            Content content = Content.builder()
                    .lectureId(lectureId)
                    .videoUrl(videoUrl)
                    .imageUrl(imageUrl)
                    .duration(duration)
                    .createdAt(LocalDateTime.now())
                    .build();

            contentRepository.save(content);

        } catch (Exception e) {
            log.error("콘텐츠 업로드 중 오류가 발생하였습니다.", e);
            throw new ContentProcessingException();
        }
    }
}

//    public InitiateUploadResponse initiateMultipartUpload(InitiateUploadRequest request) {
//        // S3에서 멀티파트 업로드를 구현
//    }