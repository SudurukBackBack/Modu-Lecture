package com.sudurukbackback.modulecture.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/main")
    public String index(Model model) {
        model.addAttribute("pageTitle", "메인 페이지");
        return "domain/main";
    }

}
