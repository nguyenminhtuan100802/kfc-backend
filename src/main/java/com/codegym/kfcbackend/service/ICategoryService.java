package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.entity.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryRequest request);
    List<Category> getAllCategories();
}
