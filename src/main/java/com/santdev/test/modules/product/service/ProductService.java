package com.santdev.test.modules.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.santdev.test.modules.product.entity.ProductEntity;
import com.santdev.test.modules.product.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductEntity> findAll() {
        return productRepository.findAll();
    }

}