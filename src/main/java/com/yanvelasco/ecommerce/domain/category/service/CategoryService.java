package com.yanvelasco.ecommerce.domain.category.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.yanvelasco.ecommerce.domain.category.dto.CategoryRequestDTO;
import com.yanvelasco.ecommerce.domain.category.dto.CategoryResponseDTO;

public interface CategoryService {
    ResponseEntity<List<CategoryResponseDTO>> getCategories();
    ResponseEntity<CategoryResponseDTO> addCategory(CategoryRequestDTO category);
    ResponseEntity<Object> deleteCategory(UUID id);
    ResponseEntity<CategoryResponseDTO> updateCategory(UUID id, CategoryRequestDTO category);
}