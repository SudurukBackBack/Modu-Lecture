package com.sudurukBackBack.Modu_Lecture.domain.lecture.controller;

import com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.response.LectureResponseDto;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.entity.Lecture;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.service.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor  // Lombok 자동 생성자 사용 → 불필요한 생성자 제거
public class LectureController {
    private final LectureService lectureService;

    @PostMapping
    public ResponseEntity<?> createLecture(@Valid @RequestBody LectureCreateRequestDto requestDto, BindingResult bindingResult) {
        // 유효성 검증 실패 시, 상세한 에러 메시지 반환
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        //  강의 생성 요청을 서비스로 전달
        Lecture createdLecture = lectureService.createLecture(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLecture);
    }

    @GetMapping("/{lecture_id}")
    public ResponseEntity<LectureResponseDto> getLecture(@PathVariable Long lecture_id) {
        //  강의 상세 조회 (없는 강의일 경우 예외 발생 처리)
        LectureResponseDto lecture = lectureService.getLecture(lecture_id);
        return ResponseEntity.ok(lecture);
    }

    @PutMapping("/{lecture_id}")
    public ResponseEntity<LectureResponseDto> updateLecture(
            @PathVariable Long lecture_id,
            @Valid @RequestBody LectureUpdateRequestDto requestDto) {

        LectureResponseDto updatedLecture = lectureService.updateLecture(lecture_id, requestDto);
        return ResponseEntity.ok(updatedLecture);
    }

}
