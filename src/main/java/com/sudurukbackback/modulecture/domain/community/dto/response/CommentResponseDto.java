package com.sudurukbackback.modulecture.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sudurukbackback.modulecture.domain.community.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//public record CommentResponseDto(
//        Long id,
//        Long userId,
//        Long postId,
//        String content,
//        LocalDateTime createdAt,
//        LocalDateTime updatedAt,
//        @JsonProperty("isActive") boolean active
//) {
//    public static CommentResponseDto fromEntity(Comment comment) {
//        return new CommentResponseDto(
//                comment.getId(),
//                comment.getUserId(),
//                comment.getPostId(),
//                comment.getContent(),
//                comment.getCreatedAt(),
//                comment.getUpdatedAt(),
//                comment.isActive()
//        );
//    }
//
//}

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public CommentResponseDto(String nickname, String content, LocalDateTime createdAt) {
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
    }
}