package com.sudurukbackback.modulecture.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/main")
    public String main(Model model) {
        return "domain/main";
    }

    @GetMapping("/mypage")
    public String mypage(Model model) {
        return "domain/mypage/mypage";
    }

    @GetMapping("/mypage-lecture")
    public String mypageLecture(Model model) {
        return "domain/mypage/mypage-lecture";
    }

    @GetMapping("/mypage-tutor")
    public String mypageTutor(Model model) {
        return "domain/mypage/mypage-tutor";
    }

    @GetMapping("/mypage-tutor-upload")
    public String mypageTutorUpload(Model model) {
        return "domain/mypage/mypage-tutor-upload";
    }

    @GetMapping("/mypage-tutor-update")
    public String mypageTutorUpdate(Model model) {
        return "domain/mypage/mypage-tutor-update";
    }

    @GetMapping("/mypage-account")
    public String mypageAccount(Model model) {
        return "domain/mypage/mypage-account";
    }


}
