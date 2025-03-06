package com.sudurukbackback.modulecture.view;

import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final LectureService lectureService;

    @GetMapping("/main")
    public String main(Model model) {
        List<Lecture> lectures = lectureService.getAllLectures();
        model.addAttribute("lectures", lectures);
        return "domain/main";
    }

}
