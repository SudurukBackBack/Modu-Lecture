package com.sudurukBackBack.Modu_Lecture.domain.storage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "content")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Integer contentId;

    @NonNull
    @Column(name = "lecture_id", nullable = false)
    private Integer lectureId;

    @NonNull
    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @NonNull
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @NonNull
    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @NonNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NonNull
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
