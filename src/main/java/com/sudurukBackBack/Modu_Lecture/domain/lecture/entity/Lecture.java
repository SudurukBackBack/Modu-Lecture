package com.sudurukBackBack.Modu_Lecture.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lectures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String instructor;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer duration;
}
