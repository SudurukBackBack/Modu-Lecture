package com.sudurukbackback.modulecture.view;

import com.sudurukbackback.modulecture.domain.community.dto.request.PostCreateRequestDto;
import com.sudurukbackback.modulecture.domain.community.dto.response.PostResponseDto;
import com.sudurukbackback.modulecture.domain.community.entity.Post;
import com.sudurukbackback.modulecture.domain.community.service.PostService;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/community")
public class CommunityController {
    private final PostService postService;
    private final UserService userService;


    // 게시글 상세
    @GetMapping("/post-detail/{id}")
    public String postDetail(@PathVariable Long id, Model model) {

        Post post = postService.getPostById(id);
        User user = userService.getUserById(post.getUserId());
        model.addAttribute("post", post);
        model.addAttribute("nickname",user.getNickname()); // 닉네임받아오는거 해야됨
        return "domain/community/post-detail";
    }

    // 전체 게시글
    @GetMapping
    public String postList(Model model,
                            @PageableDefault(page = 0, size = 4) Pageable pageable) {

        Page<Post> posts = postService.getAllPosts(pageable);
        Map<Long, String> postNicknames = posts.stream()
                .collect(Collectors.toMap(
                        Post::getId,
                        post -> userService.getUserById(post.getUserId()).getNickname()
                ));

        model.addAttribute("posts", posts);
        model.addAttribute("postNicknames", postNicknames); // Map of nicknames
        model.addAttribute("currentPage", posts.getNumber());
        model.addAttribute("totalPages", posts.getTotalPages());

        return "domain/community/post-test";
    }

    // 게시글 등록 화면
    @GetMapping("/post-write")
    public String postWrite(Model model) {
        return "domain/community/post-write";
    }

    // 게시글 등록 api 연결
    @PostMapping("/post-write")
    public String handlePostWrite(
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("content") String content,
            Authentication authentication,
            Model model) {

        // 로그인 사용자 정보 가져오기
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        // DTO로 감싸서 서비스로 전달
        PostCreateRequestDto dto = new PostCreateRequestDto(category, title, content);
        Post createdPost = postService.createPost(userId, dto);

        model.addAttribute("post", createdPost);
        return "redirect:/community"; // 글 작성 후 이동할 페이지 (예: 목록 페이지)
    }

    @GetMapping("/post-mine")
    public String postMine(Model model) {
        return "domain/community/post-mine";
    }
}
