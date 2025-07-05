package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.entity.Category;

public interface ICategoryService {
    Category createCategory(CategoryRequest request);
}
