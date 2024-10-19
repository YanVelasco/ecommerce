package com.yanvelasco.ecommerce.domain.category.service;

import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImp {

    private final List<CategoryEntity> categories = new ArrayList<>();

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public List<CategoryEntity> addCategory(CategoryEntity category) {
        categories.add(category);
        return categories;
    }

    public List<CategoryEntity> deleteCategory(UUID id) {
        categories.removeIf(c -> c.getId().equals(id));
        return categories;
    }

    public List<CategoryEntity> updateCategory(CategoryEntity category) {
        for (CategoryEntity c : categories) {
            if (c.getId().equals(category.getId())) {
                c.setName(category.getName());
            }
        }
        return categories;
    }

}
