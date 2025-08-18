package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.CategoryResponse;
import com.codegym.kfcbackend.entity.Category;
import com.codegym.kfcbackend.service.ICategoryService;
import com.codegym.kfcbackend.service.ICategoryTypeService;
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
@RequestMapping("categories")
public class CategoryController {
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest rquest) {
        try {
            Category category = categoryService.createCategory(rquest);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Create category successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();

            List<CategoryResponse> responses = new ArrayList<>();
            for (Category category : categories) {
                responses.add(CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .categoryTypeName(category.getCategoryType().getName())
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .data(responses)
                    .build());
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCategoryType(
            @PathVariable Long id,
            @RequestBody CategoryRequest request
    ) {
        try {
            Category updated = categoryService.editCategory(id, request);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Updated category successfully")
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryType(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Deleted category successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }
}
