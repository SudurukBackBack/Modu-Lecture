package com.sudurukBackBack.Modu_Lecture.domain.storage.service;
import com.sudurukBackBack.Modu_Lecture.domain.storage.exception.FFmpegExecutionException;
import com.sudurukBackBack.Modu_Lecture.domain.storage.exception.FFmpegProcessException;
import com.sudurukBackBack.Modu_Lecture.domain.storage.exception.ThumbnailGenerationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Service
@Slf4j
@RequiredArgsConstructor
public class FFmpegService {

    private final S3FileUploadService s3FileUploadService;

    @Value("${aws.s3.image-prefix}")
    private String imagePrefix;

    public void extractFramesAndUploadToS3(String videoPath, String videoS3Key) {
        try {
            // 이미지 출력 디렉토리 생성
            String tempDir = System.getProperty("java.io.tmpdir");
            String outputDirPath = tempDir + "/frames/" + System.currentTimeMillis();
            File outputDir = new File(outputDirPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // FFmpeg 명령어 구성
            String ffmpegPath = "ffmpeg";
            String[] command = {
                    ffmpegPath,
                    "-i", videoPath,
                    "-ss", "00:00:01", // 1초 시점의 프레임 추출
                    "-vframes", "1",
                    "-f", "image2",
                    outputDirPath + "/thumbnail.png"
            };

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 프로세스 출력 로그 기록
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("FFmpeg 프로세스가 오류 코드 {}로 종료되었습니다.", exitCode);
                throw new FFmpegProcessException("FFmpeg 실행 오류: 종료 코드 " + exitCode);
            } else {
                log.info("이미지 추출이 완료되었습니다.");

                // 추출된 이미지 파일을 S3에 업로드
                File thumbnailFile = new File(outputDirPath + "/thumbnail.png");
                if (thumbnailFile.exists()) {
                    // 이미지의 S3 키 생성
                    String imageFileName = videoS3Key.substring(videoS3Key.lastIndexOf("/") + 1)
                            .replace(".mp4", ".png");
                    String imageS3Key = imagePrefix + imageFileName;
                    s3FileUploadService.uploadFile(thumbnailFile, imageS3Key);
                    log.info("이미지를 S3에 업로드했습니다: {}", imageS3Key);

                    // TODO : 이미지 S3 URL을 데이터베이스에 저장
                } else {
                    log.error("썸네일 이미지 파일이 존재하지 않습니다.");
                    throw new ThumbnailGenerationException("썸네일 생성 실패: 파일이 존재하지 않습니다.");
                }
            }

        } catch (FFmpegProcessException | ThumbnailGenerationException e) {
            throw e;
        } catch (Exception e) {
            log.error("FFmpeg 실행 중 예외 발생: ", e);
            throw new FFmpegExecutionException("FFmpeg 실행 중 예외 발생", e);
        }
    }
}