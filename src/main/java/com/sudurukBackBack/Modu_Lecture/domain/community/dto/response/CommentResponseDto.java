package com.sudurukBackBack.Modu_Lecture.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sudurukBackBack.Modu_Lecture.domain.community.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        Long userId,
        Long postId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        @JsonProperty("isActive") boolean active
) {
    public static CommentResponseDto fromEntity(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUserId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.isActive()
        );
    }

}
