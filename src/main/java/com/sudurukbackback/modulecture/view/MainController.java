package com.sudurukbackback.modulecture.view;

import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureGetResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final LectureService lectureService;

    /**
     * 메인 페이지 + 검색 기능
     */
    @GetMapping("/main")
    public String mainPage(@RequestParam(value = "keyword", required = false) String keyword, Model model) {

        List<LectureGetResponseDto> lectures;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 검색 키워드가 있을 경우 강의 검색
            lectures = lectureService.searchLectures(keyword, null, null, null);
            model.addAttribute("keyword", keyword); // 검색창에 입력한 키워드 유지
        } else {
            // 검색 키워드가 없을 경우 최신 강의 목록 가져오기
            lectures = lectureService.getRecentLectures();
        }

        model.addAttribute("lectures", lectures);
        return "domain/main";
    }

}
