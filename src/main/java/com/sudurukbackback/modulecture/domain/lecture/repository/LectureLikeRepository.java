package com.sudurukbackback.modulecture.domain.lecture.repository;

import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.lecture.entity.LectureLike;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureLikeRepository extends JpaRepository<LectureLike, Long> {
    Optional<LectureLike> findByUserAndLecture(User user, Lecture lecture);
    long countByLecture(Lecture lecture);
}
