package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.IngredientCategoryResponse;
import com.codegym.kfcbackend.dto.response.IngredientResponse;
import com.codegym.kfcbackend.entity.Ingredient;
import com.codegym.kfcbackend.entity.IngredientCategory;
import com.codegym.kfcbackend.service.IIngredientCategoryService;
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
@RequestMapping("ingredient-categories")
public class IngredientCategoryController {
    private final IIngredientCategoryService ingredientCategoryService;

    public IngredientCategoryController(IIngredientCategoryService ingredientCategoryService) {
        this.ingredientCategoryService = ingredientCategoryService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<IngredientCategory> ingredientCategories = ingredientCategoryService.getAll();

            List<IngredientCategoryResponse> ingredientCategoryResponses = new ArrayList<>();
            for (IngredientCategory ingredientCategory : ingredientCategories) {
                IngredientCategoryResponse ingredientCategoryResponse = IngredientCategoryResponse.builder()
                        .id(ingredientCategory.getId())
                        .name(ingredientCategory.getName())
                        .ingredients(new ArrayList<>())
                        .build();
                for (Ingredient ingredient : ingredientCategory.getIngredients()) {
                    IngredientResponse ingredientResponse = IngredientResponse.builder()
                            .id(ingredient.getId())
                            .name(ingredient.getName())
                            .baseUnitCode(ingredient.getBaseUnitCode())
                            .averageCost(ingredient.getAverageCost())
                            .currentQuantity(ingredient.getCurrentQuantity())
                            .ingredientCategoryName(ingredient.getIngredientCategory().getName())
                            .build();
                    ingredientCategoryResponse.getIngredients().add(ingredientResponse);
                }
                ingredientCategoryResponses.add(ingredientCategoryResponse);
            }
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Lấy danh sách nhóm nguyên liệu thành công")
                            .data(ingredientCategoryResponses)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .message("Lỗi khi lấy danh sách nhóm nguyên liệu: " + e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryRequest request) {
        try {
            IngredientCategory created = ingredientCategoryService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.builder()
                            .message("Tạo nhóm nguyên liệu thành công")
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
            IngredientCategory updated = ingredientCategoryService.update(id, request);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Cập nhật nhóm nguyên liệu thành công")
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
            ingredientCategoryService.delete(id);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Xóa nhóm nguyên liệu thành công")
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
