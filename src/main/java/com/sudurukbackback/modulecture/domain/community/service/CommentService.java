package com.sudurukbackback.modulecture.domain.community.service;

import com.sudurukbackback.modulecture.domain.community.dto.request.CommentCreateRequestDto;
import com.sudurukbackback.modulecture.domain.community.dto.response.CommentResponseDto;
import com.sudurukbackback.modulecture.domain.community.entity.Comment;
import com.sudurukbackback.modulecture.domain.community.exception.CommentNotFoundException;
import com.sudurukbackback.modulecture.domain.community.repository.CommentRepository;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    public List<Comment> getAllComments(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<CommentResponseDto> getAllCommentsWithNickname(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream()
                .map(comment -> {
                    User user = userService.getUserById(comment.getUserId());
                    return new CommentResponseDto(user.getNickname(), comment.getContent(), comment.getCreatedAt());
                })
                .collect(Collectors.toList());
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
