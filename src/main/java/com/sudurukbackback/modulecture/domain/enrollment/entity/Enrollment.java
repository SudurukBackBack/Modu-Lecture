package com.sudurukbackback.modulecture.domain.enrollment.entity;

import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "enrollment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"lecture_id", "user_id"})
})
@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    // 수강 신청
    public Enrollment(User user, Lecture lecture) {
        this.user = user;
        this.lecture = lecture;
        this.enrolledAt = LocalDateTime.now();
    }
}
