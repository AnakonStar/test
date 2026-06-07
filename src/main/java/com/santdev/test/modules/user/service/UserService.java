package com.santdev.test.modules.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.santdev.test.modules.user.entity.UserEntity;
import com.santdev.test.modules.user.repository.UserRepository;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

}