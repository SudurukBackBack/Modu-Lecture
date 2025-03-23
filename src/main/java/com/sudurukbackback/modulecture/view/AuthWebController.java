package com.sudurukbackback.modulecture.view;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthWebController {

    @GetMapping("/login")
    public String login() {
        return "domain/auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "domain/auth/register";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/logout")
    public String logout() {
        return "domain/auth/logout";
    }
}
