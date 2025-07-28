package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.entity.ProductCategory;
import com.codegym.kfcbackend.repository.ProductCategoryRepository;
import com.codegym.kfcbackend.service.IProductCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService implements IProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public List<ProductCategory> getAll() {
        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        return productCategories;
    }

    @Override
    public ProductCategory create(CategoryRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Tên nhóm không được để trống");
        }
        ProductCategory existingProductCategory = productCategoryRepository.findByName(request.getName()).orElse(null);
        if (existingProductCategory != null) {
            throw new RuntimeException("Name already exists");
        }
        ProductCategory productCategory = ProductCategory.builder()
                .name(request.getName())
                .build();
        ProductCategory savedProductCategory = productCategoryRepository.save(productCategory);
        return savedProductCategory;
    }

    @Override
    public ProductCategory update(Long id, CategoryRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Tên nhóm không được để trống");
        }
        ProductCategory checkExistingName = productCategoryRepository.findByName(request.getName()).orElse(null);
        if (checkExistingName != null && !checkExistingName.getId().equals(id)) {
            throw new RuntimeException("Name already exists");
        }

        ProductCategory existingProductCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product category not found with id=" + id));
        existingProductCategory.setName(request.getName());

        ProductCategory savedProductCategory = productCategoryRepository.save(existingProductCategory);
        return savedProductCategory;
    }

    @Override
    public void delete(Long id) {
        ProductCategory existing = productCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhóm sản phẩm với id=" + id));
        if (!existing.getProducts().isEmpty()) {
            throw new RuntimeException("Không thể xóa do vẫn còn các món ăn bên trong danh mục: " + existing.getName());
        }
        productCategoryRepository.delete(existing);
    }
}
