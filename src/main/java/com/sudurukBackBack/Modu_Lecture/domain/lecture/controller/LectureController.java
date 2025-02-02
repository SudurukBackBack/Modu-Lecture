package com.sudurukBackBack.Modu_Lecture.domain.lecture.controller;

import com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.entity.Lecture;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.service.LectureService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lectures")
public class LectureController {
    private final LectureService lectureService;

    // 명시적 생성자 추가 (Lombok 없이 의존성 주입)
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @PostMapping
    public ResponseEntity<?> createLecture(@Valid @RequestBody LectureCreateRequestDto requestDto) {
        Lecture createdLecture = lectureService.createLecture(requestDto);
        return ResponseEntity.ok(createdLecture);
    }
}
