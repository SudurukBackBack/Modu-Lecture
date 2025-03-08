package com.sudurukbackback.modulecture.view;

import com.sudurukbackback.modulecture.domain.enrollment.service.EnrollmentService;
import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureGetResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.entity.Category;
import com.sudurukbackback.modulecture.domain.lecture.service.CategoryService;
import com.sudurukbackback.modulecture.domain.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final CategoryService categoryService;
    private final LectureService lectureService;
    private final EnrollmentService enrollmentService;

    // 마이페이지 = 나의 정보
    @GetMapping
    public String mypage(Model model) {
        return "domain/mypage/mypage";
    }

    // 나의 강의 관리 > 강의 등록 페이지
    @GetMapping("/tutor-upload")
    public String mypageTutorUpload(Model model) {

        // 모든 카테고리 가져오기
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories); // 'categories'를 모델에 추가

        // 빈 LectureCreateRequestDto를 모델에 추가하여 폼과 바인딩
        model.addAttribute("lectureCreateRequestDto", new LectureCreateRequestDto());

        return "domain/mypage/mypage-tutor-upload";
    }

    // 계정 관리
    @GetMapping("/account")
    public String mypageAccount(Model model) {
        return "domain/mypage/mypage-account";
    }

    // 수강 목록
    @GetMapping("/lecture")
    public String mypageLecture(Model model, Authentication auth) {
        List<LectureGetResponseDto> enrolledLectures = enrollmentService.getEnrolledLectures(auth);
        model.addAttribute("enrolledLectures", enrolledLectures);
        return "domain/mypage/mypage-lecture";
    }
    // 나의 강의 관리
    @GetMapping("/tutor")
    public String mypageTutor(Model model, Authentication auth) {
        List<LectureGetResponseDto> instructorLectures = enrollmentService.getInstructorLectures(auth);
        model.addAttribute("instructorLectures", instructorLectures);
        return "domain/mypage/mypage-tutor";
    }

    //  강의 수정 페이지 (강의 정보 불러오기)  나의 강의 관리 > 수정하기 > 강의 수정 페이지
    @GetMapping("/tutor-update")
    public String mypageTutorUpdate(@RequestParam("id") Long lectureId, Model model, Authentication auth) {
        LectureGetResponseDto lecture = enrollmentService.getLectureDetail(lectureId, auth);
        model.addAttribute("lecture", lecture);
        return "domain/mypage/mypage-tutor-update";
    }

    // 강의 수정 처리 (PATCH)
    @PatchMapping("/tutor-update/{lectureId}")
    public String updateLecture(@PathVariable Long lectureId,
                                @ModelAttribute LectureUpdateRequestDto requestDto,
                                Authentication auth) {
        enrollmentService.updateLecture(lectureId, requestDto, auth);
        return "redirect:/mypage/tutor";
    }
}