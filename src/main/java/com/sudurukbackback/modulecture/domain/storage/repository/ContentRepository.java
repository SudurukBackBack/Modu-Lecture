package com.sudurukbackback.modulecture.domain.storage.repository;

import com.sudurukbackback.modulecture.domain.storage.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Integer> {

}