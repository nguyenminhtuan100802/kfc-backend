package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.CategoryTypeRequest;
import com.codegym.kfcbackend.entity.Category;
import com.codegym.kfcbackend.entity.CategoryType;
import com.codegym.kfcbackend.repository.CategoryRepository;
import com.codegym.kfcbackend.repository.CategoryTypeRepository;
import com.codegym.kfcbackend.service.ICategoryTypeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryTypeService implements ICategoryTypeService {
    private final CategoryTypeRepository categoryTypeRepository;
    private final CategoryRepository categoryRepository;

    public CategoryTypeService(CategoryTypeRepository categoryTypeRepository, CategoryRepository categoryRepository) {
        this.categoryTypeRepository = categoryTypeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryType createCategoryType(CategoryTypeRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        String categoryTypeName = request.getName().trim();
        CategoryType existingCategoryType = categoryTypeRepository.findByName(categoryTypeName).orElse(null);
        if (existingCategoryType != null) {
            throw new RuntimeException("Category type already exists: " + categoryTypeName);
        }
        CategoryType categoryType = CategoryType.builder()
                .name(categoryTypeName)
                .build();
        CategoryType savedCategoryType = categoryTypeRepository.save(categoryType);
        return savedCategoryType;
    }

    @Override
    public CategoryType editCategoryType(Long id, CategoryTypeRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        String categoryTypeName = request.getName().trim();
        CategoryType existingCategoryType = categoryTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category type not found with id=" + id));
        if (existingCategoryType != null && !existingCategoryType.getId().equals(id)) {
            throw new RuntimeException("Category type already exists: " + categoryTypeName);
        }
        existingCategoryType.setName(categoryTypeName);
        CategoryType savedCategoryType = categoryTypeRepository.save(existingCategoryType);
        return savedCategoryType;
    }

    @Override
    @Transactional
    public void deleteCategoryType(Long id) {
        CategoryType existingCategoryType = categoryTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category type not found with id=" + id));
        List<Category> categories = categoryRepository.findByCategoryTypeId(existingCategoryType.getId());
        if (!categories.isEmpty()) {
            throw new RuntimeException("Cannot delete category type with id=" + id + " because it is used in " + categories.size() + " categories");
        }
        categoryTypeRepository.deleteById(existingCategoryType.getId());
    }

    @Override
    public List<CategoryType> getAllCategoryTypes() {
        List<CategoryType> categoryTypes = categoryTypeRepository.findAll();
        return categoryTypes;
    }
}
