package com.sudurukbackback.modulecture.domain.user.entity;

import com.sudurukbackback.modulecture.domain.user.entity.enums.UserGrade;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;
import com.sudurukbackback.modulecture.domain.user.exception.SamePasswordException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    private String nickname;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGrade grade; // 0: Bronze, 1: Silver, 2: Gold, 3: Platinum

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grade.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    // 계정 재활성화 (탈퇴 유예 기간 내 로그인, 탈퇴 요청 철회)
    public void reactiveAccount() {
        this.userStatus = UserStatus.ACTIVE;
        this.deletedAt = null;
    }

    // 비밀번호 재설정
    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {

        // 기존 비밀번호와 새 비밀번호가 동일한지 확인
        if (passwordEncoder.matches(newPassword, this.password)) {
            throw new SamePasswordException();
        }

        // 새 비밀번호를 암호화하여 저장
        this.password = passwordEncoder.encode(newPassword);
    }

    // 계정 비활성화(탈퇴) 요청 생성
    public void requestDeactivateAccount() {
        // 일정 시간이 흐르고 나서 정보를 삭제하는 것이 가능한가?
        this.userStatus = UserStatus.PENDING;
        this.deletedAt = LocalDateTime.now();
    }

    // 계정 탈퇴 처리
    public void deleteAccount() {
        // 계정 상태 변경 및 탈퇴 날짜 갱신
        this.userStatus = UserStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }
      
    // 닉네임 변경
    public void changeNickname(String newNickname) {
        // 계정 업데이트 날짜 갱신
        this.nickname = newNickname;
        this.updatedAt = LocalDateTime.now();
    }
}
