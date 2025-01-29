package com.sudurukBackBack.Modu_Lecture.domain.user.repository;

import com.sudurukBackBack.Modu_Lecture.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
