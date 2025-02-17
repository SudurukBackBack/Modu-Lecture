package com.sudurukBackBack.Modu_Lecture.domain.storage.controller;

import com.sudurukBackBack.Modu_Lecture.domain.storage.dto.request.CreateContentRequestDTO;
import com.sudurukBackBack.Modu_Lecture.domain.storage.exception.ContentUploadException;
import com.sudurukBackBack.Modu_Lecture.domain.storage.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    /**
     * 콘텐츠 파일과 메타데이터를 업로드합니다.
     *
     * @param videoFile 업로드할 비디오 파일
     * @param request   콘텐츠 메타데이터 요청 DTO
     * @return 성공 시 HTTP 200 응답
     */
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadContent(
            @RequestParam("videoFile") MultipartFile videoFile,
            @ModelAttribute CreateContentRequestDTO request) {
        try {
            contentService.uploadContent(videoFile, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // 콘텐츠 업로드 중 발생한 예외를 처리하고 커스텀 예외를 발생시킵니다.
            throw new ContentUploadException("콘텐츠 업로드에 실패하였습니다.", e);
        }
    }

    // 멀티파트 업로드 구현 (구현 중)
    // /**
    //  * 멀티파트 업로드를 시작합니다.
    //  *
    //  * @param request 업로드 시작 요청 DTO
    //  * @return 업로드 세션 정보 응답 DTO
    //  */
    // @PostMapping("/initiate-upload")
    // public ResponseEntity<InitiateUploadResponse> initiateUpload(@RequestBody InitiateUploadRequest request) {
    //     InitiateUploadResponse response = contentService.initiateMultipartUpload(request);
    //     return ResponseEntity.ok(response);
    // }
}