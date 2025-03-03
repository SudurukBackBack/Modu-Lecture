package com.sudurukbackback.modulecture.domain.lecture.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categoryrel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long lectureId;

    @NotNull
    @Column(nullable = false)
    private Long categoryId;
}
