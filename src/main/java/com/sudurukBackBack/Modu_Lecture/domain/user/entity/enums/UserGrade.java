package com.sudurukBackBack.Modu_Lecture.domain.user.entity.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum UserGrade {

    ROLE_BRONZE(0, List.of("ROLE_BRONZE")),
    ROLE_SILVER(1, List.of("ROLE_BRONZE", "ROLE_SILVER")),
    ROLE_GOLD(2, List.of("ROLE_BRONZE", "ROLE_SILVER", "ROLE_GOLD")),
    ROLE_PLATINUM(3, List.of("ROLE_BRONZE", "ROLE_SILVER", "ROLE_GOLD", "ROLE_PLATINUM"));

    private final int grade;
    private final List<String> roles;

    UserGrade(int grade, List<String> roles) {
        this.grade = grade;
        this.roles = roles;
    }
}
