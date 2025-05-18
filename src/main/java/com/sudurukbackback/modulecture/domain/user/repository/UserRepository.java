package com.sudurukbackback.modulecture.domain.user.repository;

import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    List<User> findAllByUserStatusAndDeletedAtBefore(UserStatus userStatus, LocalDateTime deleteAt);

    Page<User> findByNicknameContainingIgnoreCaseOrEmailContainingIgnoreCase(String keyword1, String keyword2, Pageable pageable);
}
