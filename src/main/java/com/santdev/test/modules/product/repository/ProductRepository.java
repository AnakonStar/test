package com.santdev.test.modules.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santdev.test.modules.product.entity.ProductEntity;

public interface ProductRepository
    extends JpaRepository<ProductEntity, Long> {
}