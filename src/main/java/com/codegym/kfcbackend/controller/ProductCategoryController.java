package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.IngredientCategoryResponse;
import com.codegym.kfcbackend.dto.response.IngredientResponse;
import com.codegym.kfcbackend.dto.response.ProductCategoryResponse;
import com.codegym.kfcbackend.dto.response.ProductResponse;
import com.codegym.kfcbackend.entity.Ingredient;
import com.codegym.kfcbackend.entity.IngredientCategory;
import com.codegym.kfcbackend.entity.Product;
import com.codegym.kfcbackend.entity.ProductCategory;
import com.codegym.kfcbackend.service.IProductCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("product-categories")
public class ProductCategoryController {
    private final IProductCategoryService productCategoryService;

    public ProductCategoryController(IProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<ProductCategory> productCategories = productCategoryService.getAll();

            List<ProductCategoryResponse> productCategoryResponses = new ArrayList<>();
            for (ProductCategory productCategory : productCategories) {
                ProductCategoryResponse productCategoryResponse = ProductCategoryResponse.builder()
                        .id(productCategory.getId())
                        .name(productCategory.getName())
                        .products(new ArrayList<>())
                        .build();
                for (Product product : productCategory.getProducts()) {
                    ProductResponse productResponse = ProductResponse.builder()
                            .name(product.getName())
                            .build();
                    productCategoryResponse.getProducts().add(productResponse);
                }
                productCategoryResponses.add(productCategoryResponse);
            }            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Lấy danh sách nhóm sản phẩm thành công")
                            .data(productCategoryResponses)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .message("Lỗi khi lấy danh sách nhóm sản phẩm: " + e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryRequest request) {
        try {
            ProductCategory created = productCategoryService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.builder()
                            .message("Tạo nhóm sản phẩm thành công")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody CategoryRequest request) {
        try {
            ProductCategory updated = productCategoryService.update(id, request);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Cập nhật nhóm sản phẩm thành công")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            productCategoryService.delete(id);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Xóa nhóm sản phẩm thành công")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
