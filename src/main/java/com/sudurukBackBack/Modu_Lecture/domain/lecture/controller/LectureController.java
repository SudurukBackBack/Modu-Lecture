package com.sudurukBackBack.Modu_Lecture.domain.lecture.controller;

import com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.response.LectureResponseDto;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.entity.Lecture;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.service.LectureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/lectures")
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @PostMapping
    public ResponseEntity<?> createLecture(@Valid @RequestBody LectureCreateRequestDto requestDto, @org.jetbrains.annotations.NotNull BindingResult bindingResult) {
        // 유효성 검증 실패 시, 상세한 에러 메시지 반환
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // 강의 생성 요청을 서비스로 전달
        Lecture createdLecture = lectureService.createLecture(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLecture);
    }

    // 생성된 강의 조회 API
    @GetMapping("/{lecture_id}")
    public ResponseEntity<LectureResponseDto> getLecture(@PathVariable Long lecture_id) {
        LectureResponseDto lecture = lectureService.getLectureById(lecture_id);
        return ResponseEntity.ok(lecture);
    }


}
