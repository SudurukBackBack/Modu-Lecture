package com.sudurukbackback.modulecture.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("pageTitle", "메인 페이지");
        return "domain/main";
    }

    @GetMapping("/mypage")
    public String mypage(Model model) {
        return "domain/mypage/mypage";
    }

    @GetMapping("/mypage-tutor-upload")
    public String mypageTutorUplaod(Model model) {
        return "domain/mypage/mypage-tutor-upload";
    }

}
