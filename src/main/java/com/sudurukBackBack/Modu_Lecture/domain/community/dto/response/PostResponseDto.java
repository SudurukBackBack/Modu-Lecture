package com.sudurukBackBack.Modu_Lecture.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sudurukBackBack.Modu_Lecture.domain.community.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponseDto {
    private final Long id;
    private final Long userId;
    private final String category;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    @JsonProperty("isActive")
    private final boolean active;

    public static PostResponseDto fromEntity(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .active(post.isActive())
                .build();
    }
}
