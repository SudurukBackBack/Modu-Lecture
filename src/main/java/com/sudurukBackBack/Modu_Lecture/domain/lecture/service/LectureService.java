package com.sudurukBackBack.Modu_Lecture.domain.lecture.service;

import com.sudurukBackBack.Modu_Lecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.entity.Lecture;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.exception.LectureCreationException;
import com.sudurukBackBack.Modu_Lecture.domain.lecture.repository.LectureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LectureService {
    private final LectureRepository lectureRepository;

    // 명시적 생성자 추가 (Lombok 없이 의존성 주입)
    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Transactional
    public Lecture createLecture(LectureCreateRequestDto requestDto) {
        try {
            Lecture lecture = new Lecture(
                    null,
                    requestDto.getTitle(),
                    requestDto.getDescription(),
                    requestDto.getInstructor(),
                    requestDto.getCategory(),
                    requestDto.getPrice(),
                    requestDto.getDuration()
            );
            return lectureRepository.save(lecture);
        } catch (Exception e) {
            throw new LectureCreationException("강의 생성 중 오류 발생");
        }
    }
}
