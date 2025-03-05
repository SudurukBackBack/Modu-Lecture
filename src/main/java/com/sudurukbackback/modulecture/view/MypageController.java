package com.sudurukbackback.modulecture.view;

import com.sudurukbackback.modulecture.domain.enrollment.service.EnrollmentService;
import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureGetResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.entity.Category;
import com.sudurukbackback.modulecture.domain.lecture.service.CategoryService;
import com.sudurukbackback.modulecture.domain.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final CategoryService categoryService;
    private final LectureService lectureService;
    private final EnrollmentService enrollmentService;

    @GetMapping
    public String mypage(Model model) {
        return "domain/mypage/mypage";
    }

    @GetMapping("/lecture")
    public String mypageLecture(Model model) {
        return "domain/mypage/mypage-lecture";
    }

    @GetMapping("/tutor")
    public String mypageTutor(Model model) {
        return "domain/mypage/mypage-tutor";
    }

    @GetMapping("/tutor-upload")
    public String mypageTutorUpload(Model model) {

        // 모든 카테고리 가져오기
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories); // 'categories'를 모델에 추가

        // 빈 LectureCreateRequestDto를 모델에 추가하여 폼과 바인딩
        model.addAttribute("lectureCreateRequestDto", new LectureCreateRequestDto());

        return "domain/mypage/mypage-tutor-upload";
    }

    @GetMapping("/tutor-update")
    public String mypageTutorUpdate(Model model) {
        return "domain/mypage/mypage-tutor-update";
    }

    @GetMapping("/account")
    public String mypageAccount(Model model) {
        return "domain/mypage/mypage-account";
    }

    @GetMapping("/lecture/{lectureId}")
    public String lecture(@PathVariable Long lectureId, Model model) {
        LectureGetResponseDto lecture = lectureService.getLecture(lectureId);
        model.addAttribute("lecture", lecture);
        return "domain/lecture/lecture";
    }

    @GetMapping("/lecture")
    public String mypageLecture(Model model, Authentication auth) {
        List<LectureGetResponseDto> enrolledLectures = enrollmentService.getEnrolledLectures(auth);
        model.addAttribute("enrolledLectures", enrolledLectures);
        return "domain/mypage/mypage-lecture";
    }

}