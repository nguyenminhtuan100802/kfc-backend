package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.entity.Category;
import com.codegym.kfcbackend.repository.CategoryRepository;
import com.codegym.kfcbackend.service.ICategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(CategoryRequest request) {
        Category existingCategory =  categoryRepository.findByName(request.getName())
                .orElse(null);
        if (existingCategory != null) {
            throw new RuntimeException(String.format(AppConstants.CATEGORY_ALREADY_EXISTS,
                    request.getName()));
        }
        Category category = Category.builder()
                .name(request.getName())
                .build();
        Category savedCategory = categoryRepository.save(category);
        return savedCategory;
    }
}
