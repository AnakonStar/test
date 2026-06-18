package com.santdev.test.modules.auth.service;

import org.springframework.stereotype.Service;

import com.santdev.test.common.security.JwtService;
import com.santdev.test.common.utils.HashUtil;
import com.santdev.test.modules.user.entity.UserEntity;
import com.santdev.test.modules.user.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;
    
    public AuthService(UserRepository repository, JwtService jwtService) {
        this.jwtService = jwtService;
        this.repository = repository;
    }

    public String login(
            String email,
            String password
    ) {

        String hash_password = HashUtil.hashPassword(password);

        UserEntity user = repository
            .findByEmail(email)
            .orElseThrow(
                () -> new RuntimeException(
                    "User not found"
                )
            );

        if (!HashUtil.comparePasswords(password, user.getPassword())) {
            throw new RuntimeException(
                "Invalid password"
            );
        }

        return jwtService.generateToken(user);
    }
}