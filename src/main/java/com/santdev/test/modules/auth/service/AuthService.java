package com.santdev.test.modules.auth.service;

import org.springframework.stereotype.Service;

import java.time.Instant;

import com.santdev.test.common.security.AuthenticatedUser;
import com.santdev.test.common.security.JwtService;
import com.santdev.test.common.utils.EncryptedIdUtil;
import com.santdev.test.common.utils.HashUtil;
import com.santdev.test.modules.auth.dto.AuthResponseDto;
import com.santdev.test.modules.auth.dto.AuthUserResponseDto;
import com.santdev.test.modules.user.entity.UserEntity;
import com.santdev.test.modules.user.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final EncryptedIdUtil encryptedIdUtil;
    
    public AuthService(UserRepository repository, JwtService jwtService, EncryptedIdUtil encryptedIdUtil) {
        this.jwtService = jwtService;
        this.repository = repository;
        this.encryptedIdUtil = encryptedIdUtil;
    }

    public AuthResponseDto login(
            String email,
            String password
    ) {

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

        return buildAuthResponse(user);
    }

    public AuthResponseDto refresh(
            String refreshToken
    ) {

        AuthenticatedUser authenticatedUser =
            jwtService.extractRefreshUser(refreshToken);

        UserEntity user = repository
            .findByEmail(authenticatedUser.getEmail())
            .orElseThrow(
                () -> new RuntimeException(
                    "User not found"
                )
            );

        return buildAuthResponse(user);
    }

    private AuthResponseDto buildAuthResponse(
            UserEntity user
    ) {
        Instant expiresAt = jwtService.getAccessTokenExpiresAt();

        return new AuthResponseDto(
            jwtService.generateAccessToken(user, expiresAt),
            jwtService.generateRefreshToken(user),
            expiresAt,
            new AuthUserResponseDto(
                encryptedIdUtil.encrypt("user", user.getId()),
                user.getName(),
                user.getEmail(),
                user.getRole().getValue()
            )
        );
    }
}
