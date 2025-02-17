package com.sudurukBackBack.Modu_Lecture.domain.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class EditorController {
    @GetMapping("/editor")
    public String writeForm() {
        return "ckeditor";
    }
}
