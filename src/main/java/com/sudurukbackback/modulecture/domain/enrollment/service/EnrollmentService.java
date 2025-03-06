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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EnrollmentService {

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;

    public void enrollInLecture(String email, Long lecture_id) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotExistException::new);

        Lecture lecture = lectureRepository.findById(lecture_id)
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

        // 사용자가 수강 중인 강의 ID 목록 가져오기
        List<Long> lectureIds = enrollmentRepository.findLectureIdsByUserId(user.getId());

        // 강의 정보를 일괄로 조회
        List<Lecture> lectures = lectureRepository.findAllById(lectureIds);

        // 조회한 강의 갯수가 맞지 않는 경우: 예외처리
        if (lectures.size() != lectureIds.size()) {
            throw new LectureNotFoundException();
        }

        // DTO 변환
        return lectures.stream()
                .map(lecture -> new LectureGetResponseDto(user.getNickname(), lecture))
                .collect(Collectors.toList());

    }
}