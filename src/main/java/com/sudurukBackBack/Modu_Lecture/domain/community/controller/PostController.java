package com.sudurukBackBack.Modu_Lecture.domain.community.controller;

import com.sudurukBackBack.Modu_Lecture.domain.community.dto.request.PostCreateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.community.dto.response.PostResponseDto;
import com.sudurukBackBack.Modu_Lecture.domain.community.entity.Post;
import com.sudurukBackBack.Modu_Lecture.domain.community.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllPosts().stream()
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(PostResponseDto.fromEntity(post));
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostCreateRequestDto postCreateDto) {
        Post createdPost = postService.createPost(postCreateDto);
        return ResponseEntity.ok(PostResponseDto.fromEntity(createdPost));
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
