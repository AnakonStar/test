package com.santdev.test.modules.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.santdev.test.common.annotations.Public;
import com.santdev.test.common.annotations.Roles;
import com.santdev.test.modules.role.enums.RoleEnum;
import com.santdev.test.modules.user.dto.CreateUserDto;
import com.santdev.test.modules.user.entity.UserEntity;
import com.santdev.test.modules.user.service.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("all")
    public List<UserEntity> findAll(@RequestParam Optional<String> name) {
        return userService.findAll(name);
    }

    @Public()
    @PostMapping()
    public UserEntity create(@RequestBody CreateUserDto data) {
        UserEntity response = userService.create(data);
        
        return response;
    }
    
}