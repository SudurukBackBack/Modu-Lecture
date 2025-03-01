package com.sudurukbackback.modulecture.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageController {

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



}
