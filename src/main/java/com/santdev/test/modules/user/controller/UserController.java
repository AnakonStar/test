package com.santdev.test.modules.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.santdev.test.common.annotations.Public;
import com.santdev.test.common.dto.GenericResponseDto;
import com.santdev.test.common.utils.EncryptedIdUtil;
import com.santdev.test.modules.user.dto.CreateUserDto;
import com.santdev.test.modules.user.entity.UserEntity;
import com.santdev.test.modules.user.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final EncryptedIdUtil encryptedIdUtil;

    public UserController(UserService userService, EncryptedIdUtil encryptedIdUtil) {
        this.userService = userService;
        this.encryptedIdUtil = encryptedIdUtil;
    }

    @GetMapping("all")
    public List<GenericResponseDto> findAll(@RequestParam Optional<String> name) {
        return userService.findAll(name)
                .stream()
                .map(this::toUserResponse)
                .toList();
    }

    @GetMapping("{encryptedId}")
    public GenericResponseDto findByEncryptedId(@PathVariable String encryptedId) {
        Long id = encryptedIdUtil.decrypt(encryptedId, "user");

        return toUserResponse(
                userService.findById(id)
        );
    }

    @Public()
    @PostMapping()
    public GenericResponseDto create(@RequestBody CreateUserDto data) {
        UserEntity response = userService.create(data);
        
        return toUserResponse(response);
    }

    private GenericResponseDto toUserResponse(UserEntity user) {
        return GenericResponseDto.fromEntity(
                user,
                List.of("id", "name", "email", "role"),
                Map.of(
                        "id", value -> encryptedIdUtil.encrypt("user", (Long) value),
                        "role", value -> user.getRole().getValue()
                )
        );
    }
    
}
