package com.sudurukbackback.modulecture.domain.community.controller;

import com.sudurukbackback.modulecture.domain.community.dto.request.CommentCreateRequestDto;
import com.sudurukbackback.modulecture.domain.community.dto.response.CommentResponseDto;
import com.sudurukbackback.modulecture.domain.community.entity.Comment;
import com.sudurukbackback.modulecture.domain.community.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    // 내가 쓴 댓글 조회
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        return ResponseEntity.ok(CommentResponseDto.fromEntity(comment));
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<String> createComment(@Valid @RequestBody CommentCreateRequestDto commentCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 오류가 있을 경우, 오류 메시지를 반환 (유효성 검증)
            String errorMessage = bindingResult.getAllErrors().getFirst().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Comment createdComment = commentService.createComment(commentCreateDto);
        System.out.println("댓글 생성 :\n"+createdComment);

        return ResponseEntity.ok("댓글 등록 성공");
    }

    // 댓글 수정은 추후 구현
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody String newContent) {
        Comment updatedComment = commentService.updateComment(id, newContent);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}
