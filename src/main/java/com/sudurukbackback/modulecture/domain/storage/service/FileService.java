package com.sudurukBackBack.Modu_Lecture.domain.storage.service;

import com.sudurukBackBack.Modu_Lecture.domain.storage.exception.LocalFileUploadException;
import com.sudurukBackBack.Modu_Lecture.domain.storage.exception.LocalFileDeleteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final String uploadDir = "${}";

    /**
     * 비디오 파일을 로컬에 저장합니다.
     *
     * @param videoFile 업로드할 비디오 파일
     * @return 저장된 비디오 파일의 로컬 경로
     */
    public String saveVideoFileLocally(MultipartFile videoFile) {
        String originalFilename = videoFile.getOriginalFilename();
        String filePath = uploadDir + File.separator + originalFilename;

        File dest = new File(filePath);
        try {
            videoFile.transferTo(dest);
        } catch (IOException e) {
            throw new LocalFileUploadException();
        }
        return filePath;
    }

    /**
     * 로컬 파일을 삭제합니다.
     *
     * @param filePath 삭제할 파일의 경로
     */
    public void deleteLocalFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                log.info("파일을 삭제할 수 없습니다: " + filePath);
                throw new LocalFileDeleteException();
            }
        }
    }
}
