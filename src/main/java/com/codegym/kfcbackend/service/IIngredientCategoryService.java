package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.entity.IngredientCategory;

import java.util.List;

public interface IIngredientCategoryService {
    List<IngredientCategory> getAll();
    IngredientCategory create(CategoryRequest request);
    IngredientCategory update(Long id, CategoryRequest request);
    void delete(Long id);
}
