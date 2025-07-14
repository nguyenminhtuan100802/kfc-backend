package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.ComboRequest;
import com.codegym.kfcbackend.dto.request.ProductRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.ComboResponse;
import com.codegym.kfcbackend.dto.response.ProductResponse;
import com.codegym.kfcbackend.dto.response.RecipeItemResponse;
import com.codegym.kfcbackend.entity.Combo;
import com.codegym.kfcbackend.entity.Product;
import com.codegym.kfcbackend.entity.RecipeItem;
import com.codegym.kfcbackend.service.IProductService;
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
@RequestMapping("products")
public class ProductController {
    private IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
        try {
            Product product = productService.createProduct(request);
            ProductResponse response = ProductResponse.builder()
                    .name(product.getName())
                    .price(product.getPrice())
                    .categoryName(product.getCategory().getName())
                    .build();
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(response)
                    .message(AppConstants.PRODUCT_CREATED_SUCCESS)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        try {
            Product product = productService.editProduct(id, request);
            ProductResponse response = ProductResponse.builder()
                    .name(product.getName())
                    .price(product.getPrice())
                    .categoryName(product.getCategory().getName())
                    .build();
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Update product successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Delete product successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping(value = "create-combo")
    public ResponseEntity<?> createCombo(@RequestBody ComboRequest request) {
        try {
            Combo combo = productService.createCombo(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message(AppConstants.COMBO_CREATED_SUCCESS)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            List<ProductResponse> productResponses = new ArrayList<>();
            for (Product product : products) {
                List<RecipeItemResponse> recipeItemResponses = new ArrayList<>();
                for (RecipeItem recipeItem : product.getRecipeItems()) {
                    recipeItemResponses.add(RecipeItemResponse.builder()
                            .ingredientName(recipeItem.getIngredient().getName())
                            .quantity(recipeItem.getQuantity())
                            .baseUnitCode(recipeItem.getIngredient().getBaseUnitCode())
                            .build());
                }
                productResponses.add(ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .imageUrl(product.getImageUrl())
                        .categoryName(product.getCategory().getName())
                        .recipeItems(recipeItemResponses)
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(productResponses)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping(value = "get-combos")
    public ResponseEntity<?> getAllCombos() {
        try {
            List<Combo> combos = productService.getAllCombos();
            List<ComboResponse> responses = new ArrayList<>();
            for (Combo combo : combos) {
                responses.add(ComboResponse.builder()
                        .name(combo.getName())
                        .description(combo.getDescription())
                        .imageUrl(combo.getImageUrl())
                        .discountAmount(combo.getDiscountAmount())
                        .totalPrice(combo.getTotalPrice())
                        .totalPriceAfterDiscount(combo.getTotalPriceAfterDiscount())
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(responses)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}
