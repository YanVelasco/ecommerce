package com.yanvelasco.ecommerce.domain.category.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;
import com.yanvelasco.ecommerce.domain.category.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryEntity>> getCategories() {
        return categoryService.getCategories();
    }


    @PostMapping("/add")
    public ResponseEntity<CategoryEntity> addCategory(@RequestBody CategoryEntity category) {
        return categoryService.addCategory(category);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable UUID id) {
        return categoryService.deleteCategory(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryEntity> updateCategory(@PathVariable UUID id, @RequestBody CategoryEntity category) {
        return categoryService.updateCategory(id, category);
    }

}