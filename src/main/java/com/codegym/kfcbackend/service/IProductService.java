package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.ProductRequest;
import com.codegym.kfcbackend.entity.Product;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductRequest request);
    List<Product> getAllProducts();
}
