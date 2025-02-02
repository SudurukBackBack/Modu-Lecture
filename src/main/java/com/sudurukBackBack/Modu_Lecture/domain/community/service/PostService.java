package com.sudurukBackBack.Modu_Lecture.domain.community.service;

import com.sudurukBackBack.Modu_Lecture.domain.community.entity.Post;
import com.sudurukBackBack.Modu_Lecture.domain.community.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Long id, String newContent) {
        Post post = getPostById(id);
        post.updateContent(newContent);
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }
}
