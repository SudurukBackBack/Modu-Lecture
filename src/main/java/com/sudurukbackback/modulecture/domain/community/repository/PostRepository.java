package com.sudurukBackBack.Modu_Lecture.domain.community.repository;

import com.sudurukBackBack.Modu_Lecture.domain.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
