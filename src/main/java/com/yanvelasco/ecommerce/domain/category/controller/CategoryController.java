package com.yanvelasco.ecommerce.domain.category.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yanvelasco.ecommerce.domain.category.dto.CategoryRequestDTO;
import com.yanvelasco.ecommerce.domain.category.dto.CategoryResponseDTO;
import com.yanvelasco.ecommerce.domain.category.dto.PagedCategoryResponseDTO;
import com.yanvelasco.ecommerce.domain.category.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<PagedCategoryResponseDTO> getCategories(
        @RequestParam(name = "pageNumber") int pageNumber,
        @RequestParam(name = "pageSize") int pageSize
    ) {
        return categoryService.getCategories(pageNumber, pageSize);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryResponseDTO> addCategory(@Valid @RequestBody CategoryRequestDTO category) {
        return categoryService.addCategory(category);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable UUID id) {
        return categoryService.deleteCategory(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable UUID id, @RequestBody CategoryRequestDTO category) {
        return categoryService.updateCategory(id, category);
    }

}