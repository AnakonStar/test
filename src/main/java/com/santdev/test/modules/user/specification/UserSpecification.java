package com.santdev.test.modules.user.specification;

import org.springframework.data.jpa.domain.Specification;

import com.santdev.test.modules.user.entity.UserEntity;

public class UserSpecification {
    public static Specification<UserEntity> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%");
        };
    }
}