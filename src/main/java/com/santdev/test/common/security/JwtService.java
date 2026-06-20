package com.santdev.test.common.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.santdev.test.modules.role.enums.RoleEnum;
import com.santdev.test.modules.user.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                SecurityConstants.SECRET
                        .getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(
            UserEntity user) {
        return generateAccessToken(user);
    }

    public String generateAccessToken(
            UserEntity user) {
        return generateAccessToken(
                user,
                getAccessTokenExpiresAt()
        );
    }

    public String generateAccessToken(
            UserEntity user,
            Instant expiresAt) {
        return generateToken(user, ACCESS_TOKEN_TYPE, expiresAt);
    }

    public String generateRefreshToken(
            UserEntity user) {
        return generateToken(
                user,
                REFRESH_TOKEN_TYPE,
                Instant.now().plusMillis(SecurityConstants.REFRESH_EXPIRATION)
        );
    }

    public Instant getAccessTokenExpiresAt() {
        return Instant.now().plusMillis(SecurityConstants.EXPIRATION);
    }

    private String generateToken(
            UserEntity user,
            String type,
            Instant expiresAt) {
        return Jwts.builder()

                .subject(
                        user.getEmail())

                .claim(
                        "id",
                        user.getId())

                .claim(
                        "role",
                        user.getRole().getValue())

                .claim(
                        "type",
                        type)

                .expiration(
                        Date.from(expiresAt))

                .signWith(getKey())

                .compact();
    }

    public Claims extractClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validate(
            String token) {

        try {

            extractClaims(token);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public AuthenticatedUser extractUser(
            String token) {

        Claims claims = extractClaims(token);
        validateTokenType(claims, ACCESS_TOKEN_TYPE);

        return buildAuthenticatedUser(claims);
    }

    public AuthenticatedUser extractRefreshUser(
            String token) {

        Claims claims = extractClaims(token);
        validateTokenType(claims, REFRESH_TOKEN_TYPE);

        return buildAuthenticatedUser(claims);
    }

    private AuthenticatedUser buildAuthenticatedUser(Claims claims) {

        String roleValue = claims.get(
                "role",
                String.class);

        Number userId = claims.get(
                "id",
                Number.class);

        return new AuthenticatedUser(

                userId.longValue(),

                claims.getSubject(),

                RoleEnum.fromValue(
                        roleValue));
    }

    private void validateTokenType(
            Claims claims,
            String expectedType) {

        String type = claims.get(
                "type",
                String.class);

        if (!expectedType.equals(type)) {
            throw new RuntimeException("Invalid token type");
        }
    }
}
