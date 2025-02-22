package com.sudurukbackback.modulecture.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sudurukbackback.modulecture.domain.community.entity.Post;

import java.time.LocalDateTime;

// record : 불변 객체를 간결하게 표현 가능 (필드가 모두 final)
public record PostResponseDto(
        Long id,
        Long userId,
        String category,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        @JsonProperty("isActive") boolean active
) {
    public static PostResponseDto fromEntity(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getUserId(),
                post.getCategory(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.isActive()
        );
    }
}
