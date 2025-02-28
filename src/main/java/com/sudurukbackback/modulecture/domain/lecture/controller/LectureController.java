package com.sudurukbackback.modulecture.domain.lecture.controller;

import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.lecture.service.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    // 강의 생성 (파일 포함)
    @PostMapping
    public ResponseEntity<?> createLecture(
            @Valid @RequestBody LectureCreateRequestDto requestDto,
            @RequestParam(value = "file", required = false) MultipartFile file, //  S3 파일 업로드를 위한 MultipartFile 추가
            BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(lectureService.createLecture(requestDto, file)); //  S3 파일 업로드 반영
    }

    // 강의 상세 조회 (파일 URL 포함)
    @GetMapping("/{lecture_id}")
    public ResponseEntity<LectureResponseDto> getLecture(@PathVariable Long lecture_id) {
        return ResponseEntity.ok(lectureService.getLecture(lecture_id));
    }

    // 강의 수정 (파일 변경 가능)
    @PutMapping("/{lecture_id}")
    public ResponseEntity<?> updateLecture(
            @PathVariable Long lecture_id,
            @Valid @RequestBody LectureUpdateRequestDto requestDto,
            @RequestParam(value = "file", required = false) MultipartFile newFile, //  S3 파일 업로드를 위한 MultipartFile 추가
            BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        LectureResponseDto updatedLecture = lectureService.updateLecture(lecture_id, requestDto, newFile); //  S3 파일 업데이트 반영
        return ResponseEntity.ok(updatedLecture);
    }

    // 강의 삭제 (파일 포함)
    @DeleteMapping("/{lecture_id}")
    public ResponseEntity<Void> deleteLecture(@PathVariable Long lecture_id) {
        lectureService.deleteLecture(lecture_id); //  S3 파일 삭제 반영
        return ResponseEntity.noContent().build();
    }
}
