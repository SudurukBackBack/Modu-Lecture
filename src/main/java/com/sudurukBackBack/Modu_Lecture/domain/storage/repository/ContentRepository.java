package com.sudurukBackBack.Modu_Lecture.domain.storage.repository;

import com.sudurukBackBack.Modu_Lecture.domain.storage.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Integer> {

}