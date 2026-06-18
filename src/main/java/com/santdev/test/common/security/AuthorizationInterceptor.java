package com.santdev.test.common.security;

import com.santdev.test.common.annotations.Public;
import com.santdev.test.common.annotations.Roles;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class AuthorizationInterceptor
        implements HandlerInterceptor {

    @Override
    public boolean preHandle(

            HttpServletRequest request,
            HttpServletResponse response,
            Object handler

    ) throws Exception {

        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        /*
         * =========================
         * PUBLIC CHECK
         * =========================
         */

        boolean isPublic =

            method.hasMethodAnnotation(
                Public.class
            )

            ||

            method

                .getBeanType()

                .isAnnotationPresent(
                    Public.class
                );

        if (isPublic) {
            return true;
        }

        /*
         * =========================
         * AUTH CHECK
         * =========================
         */

        Authentication authentication =

            SecurityContextHolder

                .getContext()

                .getAuthentication();

        if (authentication == null) {

            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Authentication required"
            );

            return false;
        }

        if (

            !(authentication.getPrincipal()
                    instanceof AuthenticatedUser)

        ) {

            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Invalid authentication"
            );

            return false;
        }

        AuthenticatedUser user =

            (AuthenticatedUser)
                authentication.getPrincipal();

        /*
         * =========================
         * ROLES CHECK
         * =========================
         */

        Roles roles =

            method.getMethodAnnotation(
                Roles.class
            );

        // procura annotation no controller

        if (roles == null) {

            roles =

                method

                    .getBeanType()

                    .getAnnotation(
                        Roles.class
                    );
        }

        if (roles != null) {

            boolean allowed =

                Arrays.stream(
                    roles.value()
                )

                .anyMatch(

                    role -> role == user.getRole()
                );

            if (!allowed) {

                response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Insufficient permissions"
                );

                return false;
            }
        }

        return true;
    }
}