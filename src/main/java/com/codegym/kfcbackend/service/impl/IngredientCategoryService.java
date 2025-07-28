package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.entity.IngredientCategory;
import com.codegym.kfcbackend.repository.IngredientCategoryRepository;
import com.codegym.kfcbackend.service.IIngredientCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientCategoryService implements IIngredientCategoryService {
    private final IngredientCategoryRepository ingredientCategoryRepository;

    public IngredientCategoryService(IngredientCategoryRepository ingredientCategoryRepository) {
        this.ingredientCategoryRepository = ingredientCategoryRepository;
    }

    @Override
    public List<IngredientCategory> getAll() {
        return ingredientCategoryRepository.findAll();
    }

    @Override
    public IngredientCategory create(CategoryRequest request) {
        String name = request.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên nhóm không được để trống");
        }
        IngredientCategory existing = ingredientCategoryRepository.findByName(name.trim()).orElse(null);
        if (existing != null) {
            throw new RuntimeException(String.format("Nhóm nguyên liệu '%s' đã tồn tại", name.trim()));
        }
        IngredientCategory category = IngredientCategory.builder()
                .name(name.trim())
                .build();
        return ingredientCategoryRepository.save(category);
    }

    @Override
    public IngredientCategory update(Long id, CategoryRequest request) {
        String name = request.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên nhóm không được để trống");
        }
        name = name.trim();
        IngredientCategory conflict = ingredientCategoryRepository.findByName(name).orElse(null);
        if (conflict != null && !conflict.getId().equals(id)) {
            throw new RuntimeException(String.format("Nhóm nguyên liệu '%s' đã tồn tại", name));
        }
        IngredientCategory existing = ingredientCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhóm nguyên liệu không tìm thấy với id=" + id));
        existing.setName(name);
        return ingredientCategoryRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        IngredientCategory existing = ingredientCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhóm nguyên liệu không tìm thấy với id=" + id));
        if (!existing.getIngredients().isEmpty()) {
            throw new RuntimeException("Không thể xóa do vẫn còn các nguyên liệu bên trong danh mục: " + existing.getName());
        }
        ingredientCategoryRepository.delete(existing);
    }
}
