package com.santdev.test.common.annotations;

import com.santdev.test.modules.role.enums.RoleEnum;

import java.lang.annotation.*;

@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Roles {

    RoleEnum[] value();

}