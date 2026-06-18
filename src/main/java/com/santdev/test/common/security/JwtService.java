package com.santdev.test.common.security;

import java.nio.charset.StandardCharsets;
import java.sql.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.santdev.test.modules.role.enums.RoleEnum;
import com.santdev.test.modules.user.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                SecurityConstants.SECRET
                        .getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(
            UserEntity user) {

        return Jwts.builder()

                .subject(
                        user.getEmail())

                .claim(
                        "id",
                        user.getId())

                .claim(
                        "role",
                        user.getRole().getValue())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + SecurityConstants.EXPIRATION))

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

        String roleValue = claims.get(
                "role",
                String.class);

        return new AuthenticatedUser(

                claims.get(
                        "id",
                        Long.class),

                claims.getSubject(),

                RoleEnum.fromValue(
                        roleValue));
    }
}