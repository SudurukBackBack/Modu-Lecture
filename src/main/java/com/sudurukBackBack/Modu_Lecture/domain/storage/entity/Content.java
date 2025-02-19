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
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private Integer lectureId;

    @NonNull
    @Column(nullable = false)
    private Integer categoryId;

    @NonNull
    @Column(nullable = false)
    private String imageUrl;

    @NonNull
    @Column(nullable = false)
    private String videoUrl;

    @NonNull
    @Column(nullable = false)
    private Integer duration;

    @NonNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
