package com.sudurukbackback.modulecture.domain.storage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "content")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private Long lectureId;

    @ElementCollection
    private List<Long> categoryIds;

    @NonNull
    @Column(nullable = false)
    private String imageUrl; // S3 스토리지 안의 이미지 객체 경로 정보

    @NonNull
    @Column(nullable = false)
    private String videoUrl; // S3 스토리지 안의 영상 객체 경로 정보

    @NonNull
    @Column(nullable = false)
    private Integer duration;

    @NonNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
