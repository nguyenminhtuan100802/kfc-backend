package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.CategoryTypeRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.CategoryResponse;
import com.codegym.kfcbackend.dto.response.CategoryTypeResponse;
import com.codegym.kfcbackend.entity.Category;
import com.codegym.kfcbackend.entity.CategoryType;
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
@RequestMapping("category-types")
public class CategoryTypeController {
    private final ICategoryTypeService categoryTypeService;
    private final ICategoryService categoryService;

    public CategoryTypeController(ICategoryTypeService categoryTypeService,
                                  ICategoryService categoryService) {
        this.categoryTypeService = categoryTypeService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> createCategoryType(@RequestBody CategoryTypeRequest rquest) {
        try {
            CategoryType categoryType = categoryTypeService.createCategoryType(rquest);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Create category type successfully")
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
    public ResponseEntity<?> getAllCategoryTypes() {
        try {
            List<CategoryType> categoryTypes = categoryTypeService.getAllCategoryTypes();

            List<CategoryTypeResponse> responses = new ArrayList<>();
            for (CategoryType categoryType : categoryTypes) {
                CategoryTypeResponse categoryTypeResponse = CategoryTypeResponse.builder()
                        .id(categoryType.getId())
                        .name(categoryType.getName())
                        .categories(new ArrayList<>())
                        .build();

                List<Category> categories = categoryService.getCategoriesByCategoryTypeId(categoryType.getId());
                for (Category category : categories){
                    categoryTypeResponse.getCategories().add(CategoryResponse.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .build());
                }

                responses.add(categoryTypeResponse);
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
            @RequestBody CategoryTypeRequest request
    ) {
        try {
            CategoryType updated = categoryTypeService.editCategoryType(id, request);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Updated category type successfully")
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
            categoryTypeService.deleteCategoryType(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Deleted category type successfully")
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
