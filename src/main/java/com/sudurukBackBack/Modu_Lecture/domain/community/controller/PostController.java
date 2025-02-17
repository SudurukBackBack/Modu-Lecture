package com.sudurukBackBack.Modu_Lecture.domain.community.controller;

import com.sudurukBackBack.Modu_Lecture.domain.community.dto.request.PostCreateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.community.dto.response.PostResponseDto;
import com.sudurukBackBack.Modu_Lecture.domain.community.entity.Post;
import com.sudurukBackBack.Modu_Lecture.domain.community.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @PageableDefault(page = 0, size = 5) Pageable pageable) { // 페이지네이션 적용
        Page<PostResponseDto> posts = postService.getAllPosts(pageable)
                .map(PostResponseDto::fromEntity);
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(PostResponseDto.fromEntity(post));
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<String> createPost(@Valid @RequestBody PostCreateRequestDto postCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 오류가 있을 경우, 오류 메시지를 반환 (유효성 검증)
            String errorMessage = bindingResult.getAllErrors().getFirst().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Post createdPost = postService.createPost(postCreateDto);
        System.out.println("게시글 생성 :\n"+createdPost);

        return ResponseEntity.ok("게시글 등록 성공");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody String newContent) {
        Post updatedPost = postService.updatePost(id, newContent);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
