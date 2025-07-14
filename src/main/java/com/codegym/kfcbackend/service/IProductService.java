package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.ComboRequest;
import com.codegym.kfcbackend.dto.request.ProductRequest;
import com.codegym.kfcbackend.entity.Combo;
import com.codegym.kfcbackend.entity.Product;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductRequest request);
    Product editProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    List<Product> getAllProducts();
    Combo createCombo(ComboRequest request);
    List<Combo> getAllCombos();
}
