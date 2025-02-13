package com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.response;

import com.sudurukBackBack.Modu_Lecture.domain.lecture.entity.Lecture;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LectureResponseDto {
    private Long lectureId;
    private Long userId;
    private String title;
    private String description;
    private String instructor;
    private int category;
    private int price;
    private LocalDateTime createdAt;

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
