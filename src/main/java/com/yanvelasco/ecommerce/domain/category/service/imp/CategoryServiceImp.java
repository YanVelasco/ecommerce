package com.yanvelasco.ecommerce.domain.category.service.imp;

import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;
import com.yanvelasco.ecommerce.domain.category.repository.CategoryRepository;
import com.yanvelasco.ecommerce.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    public ResponseEntity<List<CategoryEntity>> getCategories() {
        var categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    public ResponseEntity<CategoryEntity> addCategory(CategoryEntity category) {
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name is required");
        }
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    public ResponseEntity<Object> deleteCategory(UUID id) {
        var categoryToDelete = categoryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
        );
        categoryRepository.delete(categoryToDelete);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<CategoryEntity> updateCategory(UUID id, CategoryEntity category) {
        var categoryToUpdate = categoryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
        );
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name is required");
        }
        categoryToUpdate.setName(category.getName());
        categoryRepository.save(categoryToUpdate);
        return ResponseEntity.ok(categoryToUpdate);
    }

}