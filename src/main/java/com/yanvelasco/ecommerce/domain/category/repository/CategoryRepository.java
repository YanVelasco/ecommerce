package com.yanvelasco.ecommerce.domain.category.repository;

import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
}