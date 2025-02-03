package com.sudurukBackBack.Modu_Lecture.domain.user.entity;

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
import java.util.List;

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
    private Integer grade; // 0: Bronze, 1: Silver, 2: Gold, 3: Platinum

    @NotNull
    @Column(nullable = false)
    private Integer userStatus; // 0: INACTIVE, 1: ACTIVE

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return mapGradeToRoles(grade).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    // 상위 역할은 하위 역할의 권한도 가질 수 있도록 설정
    public List<String> mapGradeToRoles(int grade) {
        return switch (grade) {
            case 0 -> List.of("ROLE_BRONZE");
            case 1 -> List.of("ROLE_BRONZE", "ROLE_SILVER");
            case 2 -> List.of("ROLE_BRONZE", "ROLE_SILVER", "ROLE_GOLD");
            case 3 -> List.of("ROLE_BRONZE", "ROLE_SILVER", "ROLE_GOLD", "ROLE_PLATINUM");
            default -> List.of("ROLE_GUEST");
        };
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
