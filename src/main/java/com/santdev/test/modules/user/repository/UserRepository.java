package com.santdev.test.modules.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santdev.test.modules.user.entity.UserEntity;

public interface UserRepository
    extends JpaRepository<UserEntity, Long> {

}