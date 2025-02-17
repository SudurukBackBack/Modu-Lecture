package com.sudurukBackBack.Modu_Lecture.domain.lecture.service;

import com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.response.LectureResponseDto;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.entity.Lecture;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.entity.LectureStatus;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.exception.LectureNotFoundException;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Service
@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    @Transactional
    public Lecture createLecture(@NotNull LectureCreateRequestDto requestDto) {
        int category;
        try {
            category = Integer.parseInt(requestDto.getCategory()); //  String → int 변환
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("카테고리는 숫자 형식이어야 합니다.");
        }

        Lecture lecture = Lecture.builder()
                .userId(requestDto.getUserId())
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .instructor(requestDto.getInstructor())
                .category(category) //  int로 저장
                .price(requestDto.getPrice())
                .createdAt(LocalDateTime.now())
                .status(LectureStatus.ACTIVE)
                .build();

        return lectureRepository.save(lecture);
    }
    //  강의 조회 메서드
    @Transactional(readOnly = true)
    public LectureResponseDto getLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(LectureNotFoundException::new); //  변경된 코드

        return new LectureResponseDto(lecture);
    }

}
