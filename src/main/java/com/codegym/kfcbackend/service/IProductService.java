package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.ComboRequest;
import com.codegym.kfcbackend.dto.request.ProductRequest;
import com.codegym.kfcbackend.entity.Combo;
import com.codegym.kfcbackend.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductRequest request);
    Product editProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    List<Product> getAllProducts();
    Page<Product> getProductByKeyword(String keyword, int page, int size, Long productCategoryId);
}
