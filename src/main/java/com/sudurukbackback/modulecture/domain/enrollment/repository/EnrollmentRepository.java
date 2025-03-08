package com.sudurukbackback.modulecture.domain.enrollment.repository;

import com.sudurukbackback.modulecture.domain.enrollment.entity.Enrollment;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByUserAndLecture(User user, Lecture lecture);

    @Query("SELECT e.lecture.id FROM Enrollment e WHERE e.user.id = :userId")
    List<Long> findLectureIdsByUserId(@Param("userId") Long userId);

    //  강사가 등록한 강의 ID 목록 조회 (id 기준 내림차순)
    @Query("SELECT l FROM Lecture l WHERE l.instructorId = :instructorId ORDER BY l.id DESC")
    List<Lecture> findLecturesByInstructorId(@Param("instructorId") Long instructorId);
}