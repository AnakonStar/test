package com.santdev.test.modules.role.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santdev.test.modules.role.entity.RoleEntity;

public interface RoleRepository
        extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByValue(String value);
}