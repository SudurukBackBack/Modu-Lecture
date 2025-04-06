package com.sudurukbackback.modulecture.domain.lecture.entity;

import com.sudurukbackback.modulecture.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lecture_like", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "lecture_id"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    public LectureLike(User user, Lecture lecture) {
        this.user = user;
        this.lecture = lecture;
    }
}
