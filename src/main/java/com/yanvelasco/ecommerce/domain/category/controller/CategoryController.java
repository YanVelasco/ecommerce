package com.yanvelasco.ecommerce.domain.category.controller;

import com.yanvelasco.ecommerce.config.AppConstants;
import com.yanvelasco.ecommerce.domain.category.dto.request.CategoryRequestDTO;
import com.yanvelasco.ecommerce.domain.category.dto.response.CategoryResponseDTO;
import com.yanvelasco.ecommerce.domain.category.dto.response.PagedCategoryResponseDTO;
import com.yanvelasco.ecommerce.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<PagedCategoryResponseDTO> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder
    ) {
        return categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryResponseDTO> addCategory(@RequestBody @Valid CategoryRequestDTO category) {
        return categoryService.addCategory(category);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable UUID id) {
        return categoryService.deleteCategory(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable UUID id,
                                                              @RequestBody CategoryRequestDTO category) {
        return categoryService.updateCategory(id, category);
    }

}