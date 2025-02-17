package com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.response;

import com.sudurukBackBack.Modu_Lecture.domain.lecture.entity.Lecture;
import lombok.Getter;

import java.time.LocalDateTime;

public record LectureResponseDto(
        Long lectureId,
        Long userId,
        String title,
        String description,
        String instructor,
        int category,
        int price,
        LocalDateTime createdAt
) {
    public LectureResponseDto(Lecture lecture) {
        this(
                lecture.getLectureId(),
                lecture.getUserId(),
                lecture.getTitle(),
                lecture.getDescription(),
                lecture.getInstructor(),
                lecture.getCategory(),
                lecture.getPrice(),
                lecture.getCreatedAt()
        );
    }
}
