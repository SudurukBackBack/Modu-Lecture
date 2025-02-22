package com.sudurukbackback.modulecture.domain.community.service;

import com.sudurukbackback.modulecture.domain.community.dto.request.PostCreateRequestDto;
import com.sudurukbackback.modulecture.domain.community.entity.Post;
import com.sudurukbackback.modulecture.domain.community.exception.PostNotFoundException;
import com.sudurukbackback.modulecture.domain.community.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
    }

    public Post createPost(PostCreateRequestDto postCreateDto) {
        Post post = Post.builder()
                .userId(postCreateDto.getUserId())
                .category(postCreateDto.getCategory())
                .title(postCreateDto.getTitle())
                .content(postCreateDto.getContent())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
        return postRepository.save(post);
    }

    public Post updatePost(Long id, String newContent) {
        Post post = getPostById(id);
        post.updateContent(newContent);
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }
}
