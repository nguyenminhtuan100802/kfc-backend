package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.entity.Category;
import com.codegym.kfcbackend.entity.CategoryType;
import com.codegym.kfcbackend.repository.CategoryRepository;
import com.codegym.kfcbackend.repository.CategoryTypeRepository;
import com.codegym.kfcbackend.repository.ComboRepository;
import com.codegym.kfcbackend.repository.IngredientRepository;
import com.codegym.kfcbackend.repository.ProductRepository;
import com.codegym.kfcbackend.service.ICategoryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final ProductRepository productRepository;
    private final ComboRepository comboRepository;
    private final IngredientRepository ingredientRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryTypeRepository categoryTypeRepository,
                           ProductRepository productRepository,
                           ComboRepository comboRepository,
                           IngredientRepository ingredientRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryTypeRepository = categoryTypeRepository;
        this.productRepository = productRepository;
        this.comboRepository = comboRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Category createCategory(CategoryRequest request) {
        if (request.getName() == null || request.getName().isBlank() ||
                request.getCategoryTypeName() == null || request.getCategoryTypeName().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        String categoryName = request.getName().trim();
        String categoryTypeName = request.getCategoryTypeName().trim();
        CategoryType existingCategoryType = categoryTypeRepository.findByName(categoryTypeName)
                .orElseThrow(() -> new RuntimeException("Category type not found: " + categoryTypeName));

        Category existingCategory = categoryRepository.findByName(request.getName()).orElse(null);
        if (existingCategory != null) {
            throw new RuntimeException("Category already exists: " + categoryName);
        }
        Category category = Category.builder()
                .name(categoryName)
                .categoryType(existingCategoryType)
                .build();
        Category savedCategory = categoryRepository.save(category);

        return savedCategory;
    }

    @Override
    public Category editCategory(Long id, CategoryRequest request) {
        if (request.getName() == null || request.getName().isBlank() ||
                request.getCategoryTypeName() == null || request.getCategoryTypeName().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        String categoryName = request.getName().trim();
        String categoryTypeName = request.getCategoryTypeName().trim();
        CategoryType existingCategoryType = categoryTypeRepository.findByName(categoryTypeName)
                .orElseThrow(() -> new RuntimeException("Category type not found: " + categoryTypeName));

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id=" + id));
        if (existingCategory != null && !existingCategory.getId().equals(id)) {
            throw new RuntimeException("Category already exists: " + categoryName);
        }
        existingCategory.setName(categoryName);
        existingCategory.setCategoryType(existingCategoryType);
        Category savedCategory = categoryRepository.save(existingCategory);
        return savedCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id=" + id));

        long cntProduct    = productRepository.countByCategoryId(id);
        long cntCombo      = comboRepository.countByCategoryId(id);
        long cntIngredient = ingredientRepository.countByCategoryId(id);

        if (cntProduct > 0 || cntCombo > 0 || cntIngredient > 0) {
            throw new RuntimeException("Cannot delete category with id=" + id + " because it is used in " + cntProduct + " products, " + cntCombo + " combos and " + cntIngredient + " ingredients");
        }

        categoryRepository.deleteById(existingCategory.getId());
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Override
    public List<Category> getCategoriesByCategoryTypeId(Long categoryTypeId) {
        List<Category> categories = categoryRepository.findByCategoryTypeId(categoryTypeId);
        return categories;
    }
}
