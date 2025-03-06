package com.sudurukbackback.modulecture.view;

import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureGetResponseDto;
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
@RequestMapping("/e-learning")
@RequiredArgsConstructor
public class ELearningController {

    private final LectureService lectureService;

    // 강의 수강(시청) - lecture.html
    @GetMapping("/{lectureId}")
    public String lecture(@PathVariable Long lectureId, Model model) {
        LectureGetResponseDto lecture = lectureService.getLecture(lectureId);
        model.addAttribute("lecture", lecture);
        return "domain/lecture/lecture";
    }

    // 강의 정보 - lecture-detail.html
    @GetMapping("/detail/{lectureId}")
    public String lectureDetail(@PathVariable Long lectureId, Model model) {
        LectureGetResponseDto lecture = lectureService.getLecture(lectureId);
        model.addAttribute("lecture", lecture);
        return "domain/lecture/lecture-detail";
    }

}
