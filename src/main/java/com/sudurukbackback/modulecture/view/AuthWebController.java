package com.sudurukbackback.modulecture.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/web")
@Controller
public class AuthWebController {

    @GetMapping("/login")
    public String login(Model model) {
        return "/domain/auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "/domain/auth/register";
    }

}
