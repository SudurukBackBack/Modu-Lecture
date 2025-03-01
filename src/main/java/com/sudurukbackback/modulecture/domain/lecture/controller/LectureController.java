package com.sudurukbackback.modulecture.domain.lecture.controller;

import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.service.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    // 강의 생성 (파일 포함)
    @PostMapping
    public ResponseEntity<LectureResponseDto> createLecture(
            @RequestPart("requestDto") @Valid LectureCreateRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        LectureResponseDto responseDto = lectureService.createLecture(requestDto, file);
        return ResponseEntity.ok(responseDto);
    }

    // 강의 상세 조회
    @GetMapping("/{lecture_id}")
    public ResponseEntity<LectureResponseDto> getLecture(@PathVariable Long lecture_id) {
        LectureResponseDto lecture = lectureService.getLecture(lecture_id);
        return ResponseEntity.ok(lecture);
    }

    // 강의 수정 (PATCH)
    @PatchMapping("/{lecture_id}")
    public ResponseEntity<LectureResponseDto> updateLecture(
            @PathVariable Long lecture_id,
            @RequestPart("requestDto") @Valid LectureUpdateRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        LectureResponseDto updatedLecture = lectureService.updateLecture(lecture_id, requestDto, file);
        return ResponseEntity.ok(updatedLecture);
    }

    // 강의 삭제 (파일 포함)
    @DeleteMapping("/{lecture_id}")
    public ResponseEntity<Void> deleteLecture(@PathVariable Long lecture_id) {
        lectureService.deleteLecture(lecture_id);
        return ResponseEntity.noContent().build();
    }
}
