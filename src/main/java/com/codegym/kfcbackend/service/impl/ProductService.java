package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.ProductRequest;
import com.codegym.kfcbackend.entity.Category;
import com.codegym.kfcbackend.entity.Product;
import com.codegym.kfcbackend.repository.CategoryRepository;
import com.codegym.kfcbackend.repository.ProductRepository;
import com.codegym.kfcbackend.service.IProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product createProduct(ProductRequest request) {
        Category existingCategory = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.CATEGORY_NOT_FOUND,
                                request.getCategoryName())));

        Product existingProduct = productRepository.findByName(request.getName()).orElse(null);
        if (existingProduct != null) {
            throw new RuntimeException(String.format(AppConstants.PRODUCT_ALREADY_EXISTS,
                    request.getName()));
        }

        if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(AppConstants.PRODUCT_PRICE_ERROR);
        }

        if (request.getDescription() == null ||
                request.getDescription().trim().isEmpty() ||
                request.getImageUrl() == null ||
                request.getImageUrl().trim().isEmpty()) {
            throw new RuntimeException(AppConstants.INFOMATION_EMPTY);
        }

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .category(existingCategory)
                .build();

        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }
}
