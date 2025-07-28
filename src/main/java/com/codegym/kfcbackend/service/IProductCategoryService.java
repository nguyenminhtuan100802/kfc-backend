package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.entity.ProductCategory;

import java.util.List;

public interface IProductCategoryService {
    List<ProductCategory> getAll();
    ProductCategory create(CategoryRequest request);
    ProductCategory update(Long id, CategoryRequest request);
    void delete(Long id);
}
