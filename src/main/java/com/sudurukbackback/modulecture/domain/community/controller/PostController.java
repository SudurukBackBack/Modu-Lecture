package com.sudurukbackback.modulecture.domain.community.controller;

import com.sudurukbackback.modulecture.domain.community.dto.request.PostCreateRequestDto;
import com.sudurukbackback.modulecture.domain.community.dto.request.PostUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.community.dto.response.PostResponseDto;
import com.sudurukbackback.modulecture.domain.community.entity.Post;
import com.sudurukbackback.modulecture.domain.community.service.PostService;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성
public class PostController {
    private final PostService postService;

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @PageableDefault(page = 0, size = 5) Pageable pageable) { // 페이지네이션 적용
        Page<PostResponseDto> posts = postService.getAllPosts(pageable)
                .map(PostResponseDto::fromEntity);
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(PostResponseDto.fromEntity(post));
    }

    // 게시글 생성
    @PostMapping("/create")
    public ResponseEntity<String> createPost(
            @Valid @ModelAttribute PostCreateRequestDto postCreateDto,
            BindingResult bindingResult,
            Authentication auth) {
        if (bindingResult.hasErrors()) {
            // 오류가 있을 경우, 오류 메시지를 반환 (유효성 검증)
            String errorMessage = bindingResult.getAllErrors().getFirst().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        // 사용자 ID 가져오기
        User user = (User) auth.getPrincipal();
        Long userId = user.getId();

        Post createdPost = postService.createPost(userId, postCreateDto);
        log.info("게시글 생성 :\n{}", createdPost);

        return ResponseEntity.ok("게시글 등록 완료");
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequestDto postUpdateDto, BindingResult bindingResult,
            Authentication auth) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().getFirst().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        // 사용자 ID 가져오기
        User user = (User) auth.getPrincipal();
        Long userId = user.getId();

        Post updatedPost = postService.updatePost(postId, userId, postUpdateDto.getNewContent());
        log.info("게시글 수정 :\n{}", updatedPost);

        return ResponseEntity.ok("게시글 수정 완료");
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
