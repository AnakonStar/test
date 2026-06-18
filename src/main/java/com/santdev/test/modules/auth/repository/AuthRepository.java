package com.santdev.test.modules.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santdev.test.modules.auth.entity.AuthEntity;

public interface AuthRepository
    extends JpaRepository<AuthEntity, Long> {

}