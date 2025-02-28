package com.sudurukbackback.modulecture.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lecture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String instructor;

    @Column(nullable = false)
    private int category; //  기본형 int 사용

    @Column(nullable = false)
    private int price; //  기본형 int 사용

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LectureStatus status;

    //  필요한 경우 개별 setter 메서드 추가 가능
    @Setter
    @Column(nullable = false)
    private int duration; //  duration 필드 확인 및 추가
}
