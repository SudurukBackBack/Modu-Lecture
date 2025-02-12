package com.sudurukBackBack.Modu_Lecture.domain.user.entity;

import com.sudurukBackBack.Modu_Lecture.domain.user.entity.enums.UserGrade;
import com.sudurukBackBack.Modu_Lecture.domain.user.entity.enums.UserStatus;
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

    private String username; // nickname

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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grade.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public void deleteUser() {
        // 일정 시간이 흐르고 나서 정보를 삭제하는 것이 가능한가?
        this.userStatus = UserStatus.PENDING;
        this.updatedAt = LocalDateTime.now();
    }
}
