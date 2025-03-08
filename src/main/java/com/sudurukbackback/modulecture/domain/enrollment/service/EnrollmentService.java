package com.sudurukbackback.modulecture.domain.enrollment.service;

import com.sudurukbackback.modulecture.domain.enrollment.entity.Enrollment;
import com.sudurukbackback.modulecture.domain.enrollment.exception.EnrollmentAlreadyExistException;
import com.sudurukbackback.modulecture.domain.enrollment.repository.EnrollmentRepository;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureGetResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.lecture.exception.LectureNotFoundException;
import com.sudurukbackback.modulecture.domain.lecture.repository.LectureRepository;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.exception.UserNotExistException;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EnrollmentService {

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;

    public void enrollInLecture(String email, Long lectureId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotExistException::new);

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(LectureNotFoundException::new);

        if (enrollmentRepository.existsByUserAndLecture(user, lecture)) {
            throw new EnrollmentAlreadyExistException();
        }

        enrollmentRepository.save(new Enrollment(user, lecture));
    }

    public List<LectureGetResponseDto> getEnrolledLectures(Authentication auth) {
        // 현재 로그인한 사용자 정보 가져오기
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(UserNotExistException::new);

        // 사용자가 수강 중인 강의 ID만 있는 목록 가져오기
        List<Long> lectureIds = enrollmentRepository.findLectureIdsByUserId(user.getId());

        // 강의 정보를 일괄로 조회
        List<Lecture> lectures = lectureRepository.findAllById(lectureIds);

        // 조회한 강의 갯수가 맞지 않는 경우: 예외처리
        if (lectures.size() != lectureIds.size()) {
            log.info("lectureIds: " + lectureIds);
            log.info("lectures: " + lectures);
            throw new LectureNotFoundException();
        }

        // DTO 변환
        return lectures.stream()
                .map(lecture -> new LectureGetResponseDto(user.getNickname(), lecture))
                .collect(Collectors.toList());

    }
    // ✅ 강사의 강의 목록 조회 (ID 내림차순 정렬)

    public List<LectureGetResponseDto> getInstructorLectures(Authentication auth) {
        User instructor = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        List<Lecture> lectures = enrollmentRepository.findLecturesByInstructorId(instructor.getId());

        return lectures.stream()
                .map(lecture -> new LectureGetResponseDto(instructor.getNickname(), lecture))
                .toList();
    }
}