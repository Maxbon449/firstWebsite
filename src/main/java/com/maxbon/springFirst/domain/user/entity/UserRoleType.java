package com.maxbon.springFirst.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserRoleType {

    ADMIN("관리자"),
    MANAGER("매니저"),
    USER("유저");

    private final String description; // UI 에서 관리자, 매니저 같이 한글로 뜨게 해줌.

    UserRoleType(String description) {
        this.description = description;
    }
}
