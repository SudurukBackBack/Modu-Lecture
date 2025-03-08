package com.sudurukbackback.modulecture.view;

import com.sudurukbackback.modulecture.domain.community.dto.request.PostCreateRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/community")
public class CommunityController {

    @GetMapping
    public String Community(Model model) {
        return "domain/community/community";
    }

    @GetMapping("/post-detail")
    public String postDetail(Model model) {
        return "domain/community/post-detail";
    }

    @GetMapping("/post-write")
    public String postWrite(Model model) {
        model.addAttribute("postCreateDto", new PostCreateRequestDto());
        return "domain/community/post-write";
    }

    @GetMapping("/post-mine")
    public String postMine(Model model) {
        return "domain/community/post-mine";
    }
}
