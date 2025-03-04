package com.sudurukbackback.modulecture.domain.lecture.dto.response;

import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class LectureResponseDto {

    private final Long lectureId;
    private final String title;
    private final String description;
    private final int price;
    //    private final String videoUrl; //  비디오 URL 추가
//    private final String imageUrl; //  썸네일 이미지 URL 추가
    private final LocalDateTime createdAt;

    public LectureResponseDto(Lecture lecture) {
        this.lectureId = lecture.getId();
        this.title = lecture.getTitle();
        this.description = lecture.getDescription();
        this.price = lecture.getPrice();
//        this.videoUrl = videoUrl;
//        this.imageUrl = imageUrl;
        this.createdAt = lecture.getCreatedAt();
    }
}