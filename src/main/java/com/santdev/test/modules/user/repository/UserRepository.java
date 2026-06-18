package com.santdev.test.modules.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.santdev.test.modules.user.entity.UserEntity;

public interface UserRepository
    extends JpaRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {

     Optional<UserEntity> findByEmail(String email);
}