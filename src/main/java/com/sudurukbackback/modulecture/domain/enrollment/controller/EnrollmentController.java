package com.sudurukbackback.modulecture.domain.enrollment.controller;

import com.sudurukbackback.modulecture.domain.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/enroll")
@RestController
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/{lecture_id}")
    public ResponseEntity<?> enrollInLecture(
            @PathVariable Long lecture_id,
            Authentication auth
    ) {
        enrollmentService.enrollInLecture(auth.getName(), lecture_id);

        return ResponseEntity.ok("강의 등록이 완료되었습니다.");
    }
}
