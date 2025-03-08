package com.sudurukbackback.modulecture.domain.lecture.repository;

import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    // 강사의 ID로 강의 목록을 조회하고, ID 기준으로 내림차순 정렬
    List<Lecture> findByInstructorIdOrderByIdDesc(Long instructorId);
}
