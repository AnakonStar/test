package com.santdev.test.modules.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.santdev.test.common.utils.HashUtil;
import com.santdev.test.modules.role.entity.RoleEntity;
import com.santdev.test.modules.role.repository.RoleRepository;
import com.santdev.test.modules.user.dto.CreateUserDto;
import com.santdev.test.modules.user.entity.UserEntity;
import com.santdev.test.modules.user.repository.UserRepository;
import com.santdev.test.modules.user.specification.UserSpecification;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserEntity> findAll(Optional<String> name) {
        Specification<UserEntity> spec = UserSpecification.hasName(name.orElse(null));

        return userRepository.findAll(spec);
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );
    }

    public UserEntity create(
            CreateUserDto data) {

        RoleEntity role = roleRepository.findById(
                data.getId_role()).orElseThrow();

        UserEntity entity = new UserEntity();

        entity.setName(
                data.getName());

        entity.setEmail(
                data.getEmail());

        entity.setPassword(
                HashUtil.hashPassword(
                        data.getPassword()));

        entity.setRole(
                role);

        return userRepository.save(
                entity);
    }

}
