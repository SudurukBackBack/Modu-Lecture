package com.sudurukbackback.modulecture.domain.lecture.dto.response;

import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class LectureResponseDto {

    private final Long lectureId;
    private final Long userId;
    private final String title;
    private final String description;
    private final List<Long> categoryIds;
    private final int price;
    private final String videoUrl; //  비디오 URL 추가
    private final String imageUrl; //  썸네일 이미지 URL 추가
    private final LocalDateTime createdAt;

    public LectureResponseDto(Lecture lecture, String videoUrl, String imageUrl) {
        this.lectureId = lecture.getLectureId();
        this.userId = lecture.getUserId();
        this.title = lecture.getTitle();
        this.description = lecture.getDescription();
        this.categoryIds = lecture.getCategoryIds();
        this.price = lecture.getPrice();
        this.videoUrl = videoUrl;
        this.imageUrl = imageUrl;
        this.createdAt = lecture.getCreatedAt();
    }
}
