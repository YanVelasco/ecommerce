package com.yanvelasco.ecommerce.domain.category.service.imp;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;
import com.yanvelasco.ecommerce.domain.category.exceptions.AlreadyExistsException;
import com.yanvelasco.ecommerce.domain.category.exceptions.EmpytException;
import com.yanvelasco.ecommerce.domain.category.exceptions.ResourceNotFoundException;
import com.yanvelasco.ecommerce.domain.category.repository.CategoryRepository;
import com.yanvelasco.ecommerce.domain.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<List<CategoryEntity>> getCategories() {
        var categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new EmpytException("No categories found");
        }
        return ResponseEntity.ok(categories);
    }

    @Override
    public ResponseEntity<CategoryEntity> addCategory(CategoryEntity category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new AlreadyExistsException("Category already exists");
        }
        var newCategory = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @Override
    public ResponseEntity<Object> deleteCategory(UUID id) {
        var categoryToDelete = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id)
        );
        categoryRepository.delete(categoryToDelete);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CategoryEntity> updateCategory(UUID id, CategoryEntity category) {
        var categoryToUpdate = categoryRepository.findById(id)
            .filter(cat -> categoryRepository.findByName(category.getName()).isEmpty())
            .orElseThrow(() -> {
                if (categoryRepository.findByName(category.getName()).isPresent()) {
                    throw new AlreadyExistsException("Category already exists");
                } else {
                    throw new ResourceNotFoundException("Category", "id", id);
                }
            });
        categoryToUpdate.setName(category.getName());
        categoryRepository.save(categoryToUpdate);
        return ResponseEntity.ok(categoryToUpdate);
    }

}