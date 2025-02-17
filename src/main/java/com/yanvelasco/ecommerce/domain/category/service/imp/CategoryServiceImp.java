package com.yanvelasco.ecommerce.domain.category.service.imp;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.yanvelasco.ecommerce.domain.category.dto.request.CategoryRequestDTO;
import com.yanvelasco.ecommerce.domain.category.dto.response.CategoryResponseDTO;
import com.yanvelasco.ecommerce.domain.category.dto.response.PagedCategoryResponseDTO;
import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;
import com.yanvelasco.ecommerce.domain.category.mapper.CategoryMapper;
import com.yanvelasco.ecommerce.domain.category.repository.CategoryRepository;
import com.yanvelasco.ecommerce.domain.category.service.CategoryService;
import com.yanvelasco.ecommerce.exceptions.AlreadyExistsException;
import com.yanvelasco.ecommerce.exceptions.EmpytException;
import com.yanvelasco.ecommerce.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public ResponseEntity<PagedCategoryResponseDTO> getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrderBy = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrderBy);
        Page<CategoryEntity> categoriesPage = categoryRepository.findAll(pageable);

        if (categoriesPage.isEmpty()) {
            throw new EmpytException("No categories found");
        }

        PagedCategoryResponseDTO response = categoryMapper.toPagedCategoryResponseDTO(categoriesPage);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CategoryResponseDTO> addCategory(CategoryRequestDTO category) {
        if (categoryRepository.findByName(category.name()).isPresent()) {
            throw new AlreadyExistsException("Category already exists");
        }
        var newCategory = categoryRepository.save(categoryMapper.toEntity(category));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toResponseDTO(newCategory));
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
    public ResponseEntity<CategoryResponseDTO> updateCategory(UUID id, CategoryRequestDTO category) {
        var categoryToUpdate = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (category.name() != null && !category.name().isEmpty()) {
            if (categoryRepository.findByName(category.name()).isPresent()) {
                throw new AlreadyExistsException("Category already exists");
            }
            categoryToUpdate.setName(category.name());
        }

        categoryRepository.save(categoryToUpdate);
        return ResponseEntity.ok(categoryMapper.toResponseDTO(categoryToUpdate));
    }
}
