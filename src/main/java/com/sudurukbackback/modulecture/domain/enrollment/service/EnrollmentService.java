package com.sudurukbackback.modulecture.domain.enrollment.service;

import com.sudurukbackback.modulecture.domain.enrollment.entity.Enrollment;
import com.sudurukbackback.modulecture.domain.enrollment.exception.EnrollmentAlreadyExistException;
import com.sudurukbackback.modulecture.domain.enrollment.repository.EnrollmentRepository;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.lecture.exception.LectureNotFoundException;
import com.sudurukbackback.modulecture.domain.lecture.repository.LectureRepository;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.exception.UserNotExistException;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
