package com.sudurukbackback.modulecture.domain.user.entity;

import com.sudurukbackback.modulecture.domain.enrollment.entity.Enrollment;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.user.entity.enums.ProfileField;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserGrade;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;
import com.sudurukbackback.modulecture.global.exception.BasicServerException;
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

    private String socialId;

    private String socialType;

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
    private UserGrade grade;

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
     * 제공된 상세 정보를 사용하여 새로운 User 엔티티를 생성합니다.
     *
     * @param email 사용자의 이메일 주소
     * @param password 사용자의 비밀번호
     * @param nickname 사용자의 닉네임
     * @param grade 사용자의 등급을 나타내는 UserGrade
     * @return User 엔티티의 새로운 인스턴스
     */
    public static User createUserEntity(String email, String password, String nickname, UserGrade grade) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .grade(grade)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 제공된 세부 정보를 사용하여 네이버 소셜 로그인에 특화된 새로운 User 인스턴스를 생성합니다.
     *
     * @param userId 네이버 플랫폼에서 제공하는 사용자의 고유 식별자
     * @param email 사용자의 이메일 주소
     * @param nickname 사용자의 닉네임, null인 경우 이메일이 닉네임으로 사용됨
     * @param type 소셜 로그인 플랫폼 유형(예: "NAVER")
     * @return 제공된 데이터로 초기화된 새로운 User 인스턴스
     */
    public static User createNaver(String userId, String email, String nickname, String type) {
        return User.builder()
                .socialId(userId)
                .socialType(type)
                .email(email)
                .password("<PASSWORD>") // 소셜 로그인의 경우 비밀번호 의미 없음
                .nickname(nickname == null ? email : nickname)
                .grade(UserGrade.ROLE_BRONZE)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 계정 재활성화 (탈퇴 유예 기간 내 로그인, 탈퇴 요청 철회)
     */
    public void reactiveAccount() {
        this.userStatus = UserStatus.ACTIVE;
        this.deletedAt = null;
        trackUpdate();
    }

    /**
     * 사용자 계정을 비활성화합니다. 사용자 상태를 업데이트하고 계정 삭제 타임스탬프를 설정합니다.
     *
     * @param status 사용자 계정에 설정할 새로운 상태이며, 일반적으로 비활성화 상태를 나타냅니다.
     */
    public void deactivateAccount(UserStatus status) {
        // 계정 상태 변경 및 탈퇴 날짜 갱신
        this.userStatus = status;
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 주어진 값으로 사용자의 특정 프로필 필드를 업데이트합니다.
     *
     * @param field 업데이트할 프로필 필드이며, {@link ProfileField}로 지정됩니다.
     * @param value 지정된 필드에 할당할 새로운 값 (필드에 따라 타입 캐스팅됨)
     * @throws BasicServerException 제공된 필드가 지원하지 않는 {@link ProfileField}인 경우
     */
    public void updateProfile(ProfileField field, Object value) {
        switch (field) {
            case PASSWORD -> this.password = (String) value;
            case NICKNAME -> this.nickname = (String) value;
            case GRADE -> this.grade = (UserGrade) value;
            case STATUS -> this.userStatus = (UserStatus) value;
            default -> throw new BasicServerException();
        }
        trackUpdate();
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

    // 정보 수정 시점 기록
    private void trackUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
