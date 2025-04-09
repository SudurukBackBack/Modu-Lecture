package com.sudurukbackback.modulecture.view;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/web/v1/auth")
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

    @GetMapping("/refresh")
    public String refresh() {
        return "domain/auth/refresh";
    }
}
