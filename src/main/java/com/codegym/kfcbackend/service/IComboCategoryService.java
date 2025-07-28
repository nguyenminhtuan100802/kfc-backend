package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.entity.ComboCategory;

import java.util.List;

public interface IComboCategoryService {
    List<ComboCategory> getAll();
    ComboCategory create(CategoryRequest req);
    ComboCategory update(Long id, CategoryRequest req);
    void delete(Long id);
}
