package com.sudurukbackback.modulecture.domain.storage.service;

import com.sudurukbackback.modulecture.domain.storage.exception.ContentProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentInfoService {

    /**
     * 비디오 파일의 재생 시간을 초 단위로 추출합니다.
     *
     * @param videoFilePath 비디오 파일의 로컬 경로
     * @return 비디오 재생 시간 (초)
     */
    public int getVideoDurationInSeconds(String videoFilePath) {
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
            throw new ContentProcessingException();
        }
        return duration;
    }

    /**
     * 비디오 파일로부터 썸네일 이미지를 생성합니다.
     *
     * @param videoFilePath 비디오 파일의 로컬 경로
     * @return 생성된 썸네일 이미지의 로컬 경로
     */
    public String generateThumbnail(String videoFilePath) {
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
            throw new ContentProcessingException();
        }
        return thumbnailPath;
    }


}
