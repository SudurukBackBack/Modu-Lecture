package com.sudurukbackback.modulecture.domain.community.service;

import com.sudurukbackback.modulecture.domain.community.dto.request.PostCreateRequestDto;
import com.sudurukbackback.modulecture.domain.community.entity.Post;
import com.sudurukbackback.modulecture.domain.community.exception.PostNotFoundException;
import com.sudurukbackback.modulecture.domain.community.exception.UnauthorizedException;
import com.sudurukbackback.modulecture.domain.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
    }

    // 게시글 생성
    public Post createPost(Long userId, PostCreateRequestDto postCreateDto) {
        Post post = Post.builder()
                .userId(userId)
                .category(postCreateDto.getCategory())
                .title(postCreateDto.getTitle())
                .content(postCreateDto.getContent())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
        return postRepository.save(post);
    }

    public Post updatePost(Long id, Long userId, String newContent) {
        Post post = getPostById(id);
        // 권한 검사
        if (!post.getUserId().equals(userId)) {
            throw new UnauthorizedException();
        }
        String cleanContent = HtmlUtils.htmlEscape(newContent); // XSS 공격 방지
        post.updateContent(cleanContent);
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }
}
