package com.sudurukbackback.modulecture.domain.enrollment.controller;

import com.sudurukbackback.modulecture.domain.enrollment.service.EnrollmentService;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureGetResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    private final EnrollmentService enrollmentService;

    public MyPageController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/lectures")
    public String myLectures(Model model, Authentication auth) {
        String email = auth.getName();
        List<LectureGetResponseDto> lectures = enrollmentService.getEnrolledLectures(email);
        model.addAttribute("lectures", lectures);
        return "mypage-lecture"; // Thymeleaf 템플릿 (mypage-lecture.html)으로 이동
    }
}
