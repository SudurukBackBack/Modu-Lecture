package com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.response;

import com.sudurukBackBack.Modu_Lecture.domain.lecture.entity.Lecture;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LectureResponseDto {
    private final Long lectureId;
    private final Long userId;
    private final String title;
    private final String description;
    private final String instructor;
    private final int category;
    private final int price;
    private final LocalDateTime createdAt;

    public LectureResponseDto(Lecture lecture) {
        this.lectureId = lecture.getLectureId();
        this.userId = lecture.getUserId();
        this.title = lecture.getTitle();
        this.description = lecture.getDescription();
        this.instructor = lecture.getInstructor();
        this.category = lecture.getCategory();
        this.price = lecture.getPrice();
        this.createdAt = lecture.getCreatedAt();
    }
}
