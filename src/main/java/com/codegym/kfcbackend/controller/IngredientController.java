package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.IngredientRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.entity.Ingredient;
import com.codegym.kfcbackend.service.IIngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("ingredients")
public class IngredientController {
    private final IIngredientService ingredientService;

    public IngredientController(IIngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<?> createIngredient(@RequestBody IngredientRequest request) {
        try {
            Ingredient ingredient = ingredientService.createIngredient(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(ingredient)
                    .message("Create ingredient successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllIngredients() {
        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ApiResponse.builder()
                .data(ingredients)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateIngredient(@PathVariable Long id, @RequestBody IngredientRequest request) {
        try {
            Ingredient ingredient = ingredientService.editIngredient(id, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(ingredient)
                    .message("Update ingredient successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteIngredient(@PathVariable Long id) {
        try {
            ingredientService.deleteIngredient(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Xoá nguyên liệu thành công")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}
