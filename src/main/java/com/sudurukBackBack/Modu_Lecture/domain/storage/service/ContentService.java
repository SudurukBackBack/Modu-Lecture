package com.sudurukBackBack.Modu_Lecture.domain.storage.service;

import com.sudurukBackBack.Modu_Lecture.domain.storage.dto.request.UploadContentRequestDTO;
import com.sudurukBackBack.Modu_Lecture.domain.storage.entity.Content;
import com.sudurukBackBack.Modu_Lecture.domain.storage.exception.ContentProcessingException;
import com.sudurukBackBack.Modu_Lecture.domain.storage.exception.FileStorageException;
import com.sudurukBackBack.Modu_Lecture.domain.storage.exception.S3UploadException;
import com.sudurukBackBack.Modu_Lecture.domain.storage.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final S3Client s3Client;
    private final String bucketName = "${cloud.aws.s3.bucket-name}"; // 실제 버킷 이름으로 변경하세요.

    /**
     * 콘텐츠를 업로드하고 처리합니다.
     *
     * @param videoFile 업로드할 비디오 파일
     * @param request   콘텐츠 생성 요청 DTO
     */
    public void uploadContent(MultipartFile videoFile, UploadContentRequestDTO request) {
        try {
            // 1. 비디오 파일을 로컬에 저장
            String localVideoPath = saveVideoFileLocally(videoFile);

            // 2. 동영상 재생 시간 추출
            int duration = getVideoDurationInSeconds(localVideoPath);

            // 3. 썸네일 이미지 생성
            String thumbnailPath = generateThumbnail(localVideoPath);

            // 4. 동영상 파일을 S3에 업로드
            String videoS3Key = "video/" + UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
            String videoUrl = uploadFileToS3(localVideoPath, videoS3Key);

            // 5. 썸네일 이미지를 S3에 업로드
            String imageS3Key = "image/" + UUID.randomUUID() + "_" + Paths.get(thumbnailPath).getFileName();
            String imageUrl = uploadFileToS3(thumbnailPath, imageS3Key);

            // 6. 로컬 파일 삭제
            deleteLocalFile(localVideoPath);
            deleteLocalFile(thumbnailPath);

            // 7. Content 엔티티 생성
            Content content = Content.builder()
                    .lectureId(request.getLectureId())
                    .categoryId(request.getCategoryId())
                    .videoUrl(videoUrl)
                    .imageUrl(imageUrl)
                    .duration(duration)
                    .createdAt(LocalDateTime.now())
                    .build();

            contentRepository.save(content);

        } catch (Exception e) {
            throw new ContentProcessingException("콘텐츠 업로드 중 오류가 발생하였습니다.", e);
        }
    }

    /**
     * 비디오 파일을 로컬에 저장합니다.
     *
     * @param videoFile 업로드할 비디오 파일
     * @return 저장된 비디오 파일의 로컬 경로
     */
    private String saveVideoFileLocally(MultipartFile videoFile) {
        String uploadDir = "path/to/local/storage";
        String originalFilename = videoFile.getOriginalFilename();
        String filePath = uploadDir + File.separator + originalFilename;

        File dest = new File(filePath);
        try {
            videoFile.transferTo(dest);
        } catch (IOException e) {
            throw new FileStorageException("비디오 파일 저장에 실패하였습니다.", e);
        }
        return filePath;
    }

    /**
     * 비디오 파일의 재생 시간을 초 단위로 추출합니다.
     *
     * @param videoFilePath 비디오 파일의 로컬 경로
     * @return 비디오 재생 시간 (초)
     */
    private int getVideoDurationInSeconds(String videoFilePath) {
        int duration;
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe",
                    "-v", "error",
                    "-show_entries", "format=duration",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    videoFilePath);
            Process process = pb.start();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s = stdInput.readLine();
            duration = (int) Math.round(Double.parseDouble(s));

            process.waitFor();

        } catch (IOException | InterruptedException e) {
            throw new ContentProcessingException("비디오 재생 시간 추출에 실패하였습니다.", e);
        }
        return duration;
    }

    /**
     * 비디오 파일로부터 썸네일 이미지를 생성합니다.
     *
     * @param videoFilePath 비디오 파일의 로컬 경로
     * @return 생성된 썸네일 이미지의 로컬 경로
     */
    private String generateThumbnail(String videoFilePath) {
        String thumbnailPath = videoFilePath.replace(".mp4", "_thumbnail.jpg");
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-ss", "00:00:01",
                    "-i", videoFilePath,
                    "-vframes", "1",
                    "-q:v", "2",
                    thumbnailPath);
            Process process = pb.start();
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            throw new ContentProcessingException("썸네일 생성에 실패하였습니다.", e);
        }
        return thumbnailPath;
    }

    /**
     * 로컬 파일을 S3에 업로드합니다.
     *
     * @param localFilePath 업로드할 로컬 파일 경로
     * @param s3Key         S3 버킷 내의 파일 키
     * @return 업로드된 파일의 S3 URL
     */
    public String uploadFileToS3(String localFilePath, String s3Key) {
        File file = new File(localFilePath);
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            return s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(s3Key)).toExternalForm();

        } catch (Exception e) {
            throw new S3UploadException("S3 업로드에 실패하였습니다.", e);
        }
    }

    /**
     * 로컬 파일을 삭제합니다.
     *
     * @param filePath 삭제할 파일의 경로
     */
    private void deleteLocalFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                log.info("파일을 삭제할 수 없습니다: " + filePath);
            }
        }
    }
}

//    public InitiateUploadResponse initiateMultipartUpload(InitiateUploadRequest request) {
//        // S3에서 멀티파트 업로드를 구현
//    }