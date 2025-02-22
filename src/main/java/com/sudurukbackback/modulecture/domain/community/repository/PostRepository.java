package com.sudurukbackback.modulecture.domain.community.repository;

import com.sudurukbackback.modulecture.domain.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
