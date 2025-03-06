package com.sudurukbackback.modulecture.domain.enrollment.controller;

import com.sudurukbackback.modulecture.domain.enrollment.exception.EnrollmentAlreadyExistException;
import com.sudurukbackback.modulecture.domain.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enroll")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/{lectureId}")
    public ResponseEntity<?> enrollInLecture(@PathVariable Long lectureId, Authentication auth) {
        try {
            enrollmentService.enrollInLecture(auth.getName(), lectureId);
            return ResponseEntity.ok("수강 신청이 완료되었습니다.");
        } catch (EnrollmentAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 수강 신청한 강의입니다.");
        }
    }
}