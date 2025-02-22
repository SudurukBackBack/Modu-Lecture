package com.sudurukBackBack.Modu_Lecture.domain.lecture.repository;

import com.sudurukBackBack.Modu_Lecture.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
