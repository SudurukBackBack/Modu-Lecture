package com.sudurukbackback.modulecture.domain.lecture.dto.response;

import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import org.jetbrains.annotations.NotNull;

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
    public LectureResponseDto(@NotNull Lecture lecture) {
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
