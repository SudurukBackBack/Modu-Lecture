package com.sudurukbackback.modulecture.domain.lecture.entity;

import com.sudurukbackback.modulecture.domain.enrollment.entity.Enrollment;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "lecture")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long instructorId;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT", nullable = false) // 긴 문장 저장이 가능하도록 TEXT 데이터 타입 미리 지정
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LectureStatus status;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    // 강사와 연관된 관계 설정
    @ManyToOne(fetch = FetchType.LAZY) // lazy 로딩을 사용해 성능 향상
    @JoinColumn(name = "instructorId", referencedColumnName = "id", insertable = false, updatable = false) // instructorId와 User의 id 매핑
    private User instructor;

    // 수강 중인 사용자 목록
    public List<User> getEnrolledUsers() {
        return enrollments.stream()
                .map(Enrollment::getUser)
                .collect(Collectors.toList());
    }
}