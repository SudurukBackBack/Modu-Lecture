package com.sudurukbackback.modulecture.domain.community.service;

import com.sudurukbackback.modulecture.domain.community.dto.request.CommentCreateRequestDto;
import com.sudurukbackback.modulecture.domain.community.entity.Comment;
import com.sudurukbackback.modulecture.domain.community.exception.CommentNotFoundException;
import com.sudurukbackback.modulecture.domain.community.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getAllComments(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);
    }

    public Comment createComment(CommentCreateRequestDto commentCreateDto) {
        Comment comment = Comment.builder()
                .userId(commentCreateDto.getUserId())
                .postId(commentCreateDto.getPostId())
                .content(commentCreateDto.getContent())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long id, String newContent) {
        Comment comment = getCommentById(id);
        comment.updateContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        Comment comment = getCommentById(id);
        commentRepository.delete(comment);
    }
}
