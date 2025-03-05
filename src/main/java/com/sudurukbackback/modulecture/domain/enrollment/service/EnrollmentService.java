package com.sudurukbackback.modulecture.domain.enrollment.service;

import com.sudurukbackback.modulecture.domain.enrollment.entity.Enrollment;
import com.sudurukbackback.modulecture.domain.enrollment.repository.EnrollmentRepository;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureGetResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.lecture.repository.LectureRepository;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.exception.UserNotExistException;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EnrollmentService {

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;

    // 사용자가 수강중인 강의 목록 조회
    @Transactional(readOnly = true)
    public List<LectureGetResponseDto> getEnrolledLectures(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotExistException::new);

        List<Enrollment> enrollments = enrollmentRepository.findByUser(user);

        return enrollments.stream()
                .map(enrollment -> {
                    Lecture lecture = enrollment.getLecture();
                    return new LectureGetResponseDto(
                            lecture.getInstructorId().toString(), // 강사 ID를 String으로 변환
                            lecture
                    );
                })
                .collect(Collectors.toList());
    }
}
