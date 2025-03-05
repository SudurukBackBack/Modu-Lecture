package com.sudurukbackback.modulecture.domain.enrollment.repository;

import com.sudurukbackback.modulecture.domain.enrollment.entity.Enrollment;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByUserAndLecture(User user, Lecture lecture);

    // 특정 사용자의 수강 강의 목록 조회
    List<Enrollment> findByUser(User user);
}