package com.santdev.test.common.security;

import com.santdev.test.modules.role.enums.RoleEnum;

public class AuthenticatedUser {

    private Long id;

    private String email;

    private RoleEnum role;

    public AuthenticatedUser(
            Long id,
            String email,
            RoleEnum role
    ) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public RoleEnum getRole() {
        return role;
    }
}