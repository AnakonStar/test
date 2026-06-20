package com.santdev.test.modules.auth.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponseDto {

    private String token;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_at")
    private Instant expiresAt;

    private AuthUserResponseDto user;

    public AuthResponseDto(
            String token,
            String refreshToken,
            Instant expiresAt,
            AuthUserResponseDto user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(
            String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public AuthUserResponseDto getUser() {
        return user;
    }

    public void setUser(AuthUserResponseDto user) {
        this.user = user;
    }
}
