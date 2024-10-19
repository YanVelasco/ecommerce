package com.yanvelasco.ecommerce.domain.category.service;

import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    ResponseEntity<List<CategoryEntity>> getCategories();
    ResponseEntity<CategoryEntity> addCategory(CategoryEntity category);
    ResponseEntity<Object> deleteCategory(UUID id);
    ResponseEntity<CategoryEntity> updateCategory(UUID id, CategoryEntity category);
}