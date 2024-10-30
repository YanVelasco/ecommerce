package com.yanvelasco.ecommerce.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yanvelasco.ecommerce.domain.product.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}