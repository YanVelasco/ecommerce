package com.yanvelasco.ecommerce.domain.category.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.yanvelasco.ecommerce.domain.category.dto.request.CategoryRequestDTO;
import com.yanvelasco.ecommerce.domain.category.dto.response.CategoryResponseDTO;
import com.yanvelasco.ecommerce.domain.category.dto.response.PagedCategoryResponseDTO;

public interface CategoryService {
    ResponseEntity<PagedCategoryResponseDTO> getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);   
     ResponseEntity<CategoryResponseDTO> addCategory(CategoryRequestDTO category);
    ResponseEntity<Object> deleteCategory(UUID id);
    ResponseEntity<CategoryResponseDTO> updateCategory(UUID id, CategoryRequestDTO category);
}