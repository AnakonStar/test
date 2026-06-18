package com.santdev.test.modules.role.enums;

import lombok.Getter;

@Getter()
public enum RoleEnum {

    ADMIN("admin"),
    USER("user");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }

    public static RoleEnum fromValue(String value) {
        for (RoleEnum role : values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }

        throw new IllegalArgumentException(
            "Invalid role: " + value
        );
    }
}