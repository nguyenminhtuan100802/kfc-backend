package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.CategoryTypeRequest;
import com.codegym.kfcbackend.entity.CategoryType;

import java.util.List;

public interface ICategoryTypeService {
    CategoryType createCategoryType(CategoryTypeRequest request);
    CategoryType editCategoryType(Long id, CategoryTypeRequest request);
    void deleteCategoryType(Long id);
    List<CategoryType> getAllCategoryTypes();
}
