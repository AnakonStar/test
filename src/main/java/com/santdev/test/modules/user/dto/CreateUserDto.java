package com.santdev.test.modules.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter()
@Setter()
public class CreateUserDto {
    private String email;
 
    private String name;

    private String password;

    private Long id_role;
}