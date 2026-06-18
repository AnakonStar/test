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
        String token = authService.login(
            request.getEmail(),
            request.getPassword()
        );

        return new AuthResponseDto(token);
    }
}