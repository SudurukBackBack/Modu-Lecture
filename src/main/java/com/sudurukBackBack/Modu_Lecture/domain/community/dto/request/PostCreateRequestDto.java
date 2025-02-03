package com.sudurukBackBack.Modu_Lecture.domain.community.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequestDto {
    private Long userId;
    private String category;
    private String title;
    private String content;
}
