package com.santdev.test.modules.auth.controller;

import org.springframework.web.bind.annotation.*;

import com.santdev.test.common.annotations.Public;
import com.santdev.test.modules.auth.dto.*;
import com.santdev.test.modules.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Public()
    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto request) {
        return authService.login(
            request.getEmail(),
            request.getPassword()
        );
    }

    @Public()
    @PostMapping("/refresh")
    public AuthResponseDto refresh(@RequestBody RefreshTokenRequestDto request) {
        return authService.refresh(
            request.getRefreshToken()
        );
    }
}
