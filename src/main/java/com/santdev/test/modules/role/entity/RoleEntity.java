package com.santdev.test.modules.role.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome amigável
    @Column(nullable = false)
    private String name;

    // Valor interno do sistema
    @Column(nullable = false, unique = true)
    private String value;

}