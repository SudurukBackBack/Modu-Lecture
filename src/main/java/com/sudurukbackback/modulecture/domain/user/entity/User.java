package com.sudurukbackback.modulecture.domain.user.entity;

import com.sudurukbackback.modulecture.domain.enrollment.entity.Enrollment;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
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
import java.util.ArrayList;
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

    private String nickname;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGrade grade; // 0: Bronze, 1: Silver, 2: Gold, 3: Platinum

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

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

    /**
     * 계정 재활성화 (탈퇴 유예 기간 내 로그인, 탈퇴 요청 철회)
     */
    public void reactiveAccount() {
        this.userStatus = UserStatus.ACTIVE;
        this.deletedAt = null;
    }

    /**
     * 비밀번호 변경
     *
     * @param newPassword 새 비밀번호
     * @param passwordEncoder PasswordEncoder
     */
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
        this.userStatus = UserStatus.PENDING;
        this.deletedAt = LocalDateTime.now();
    }

    // 계정 탈퇴 처리
    public void deactivateAccount() {
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

    // 수강 신청 추가 메서드
    public void enrollInLecture(Lecture lecture) {
        Enrollment enrollment = new Enrollment(this, lecture);
        this.enrollments.add(enrollment);
    }

    // 사용자 승급
    public void upgradeGrade() {
        this.grade = this.grade.nextGrade();
    }
}
