package com.sudurukBackBack.Modu_Lecture.domain.community.service;

import com.sudurukBackBack.Modu_Lecture.domain.community.dto.request.CommentCreateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.community.entity.Comment;
import com.sudurukBackBack.Modu_Lecture.domain.community.exception.CommentNotFoundException;
import com.sudurukBackBack.Modu_Lecture.domain.community.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

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
