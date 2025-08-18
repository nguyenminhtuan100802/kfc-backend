package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.ProductRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.ProductResponse;
import com.codegym.kfcbackend.dto.response.RecipeItemResponse;
import com.codegym.kfcbackend.entity.Product;
import com.codegym.kfcbackend.entity.RecipeItem;
import com.codegym.kfcbackend.service.IProductService;
import com.codegym.kfcbackend.service.IRecipeItemService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {
    private final IProductService productService;
    private final IRecipeItemService recipeItemService;

    public ProductController(IProductService productService,
                             IRecipeItemService recipeItemService) {
        this.productService = productService;
        this.recipeItemService = recipeItemService;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
        try {
            Product product = productService.createProduct(request);
            ProductResponse response = ProductResponse.builder()
                    .name(product.getName())
                    .price(product.getPrice())
//                    .categoryName(product.getProductCategory().getName())
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
//                    .categoryName(product.getProductCategory().getName())
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

    @GetMapping(value = "all")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();

            List<ProductResponse> productResponses = new ArrayList<>();
            for (Product product : products) {
                ProductResponse productResponse = ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .imageUrl(product.getImageUrl())
                        .categoryName(product.getProductCategory().getName())
                        .recipeItems(new ArrayList<>())
                        .build();

                List<RecipeItem> recipeItems = recipeItemService.getAllRecipeItemsByProductId(product.getId());
                for (RecipeItem recipeItem : recipeItems) {
                    productResponse.getRecipeItems().add(RecipeItemResponse.builder()
                            .ingredientName(recipeItem.getIngredient().getName())
                            .quantity(recipeItem.getQuantity())
                            .baseUnitCode(recipeItem.getIngredient().getBaseUnitCode())
                            .build());
                }
                productResponses.add(productResponse);
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

    @GetMapping
    public ResponseEntity<?> getPageProduct(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size,
            @RequestParam(value = "productCategoryId", required = false) Long productCategoryId
    ) {
        try {
            Page<Product> result = productService.getProductByKeyword(keyword, page, size, productCategoryId);

            List<Product> products = result.getContent();
            List<ProductResponse> productResponses = new ArrayList<>();
            for (Product product : products) {
                ProductResponse productResponse = ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .imageUrl(product.getImageUrl())
                        .categoryName(product.getProductCategory().getName())
                        .recipeItems(new ArrayList<>())
                        .build();

                List<RecipeItem> recipeItems = recipeItemService.getAllRecipeItemsByProductId(product.getId());
                for (RecipeItem recipeItem : recipeItems) {
                    productResponse.getRecipeItems().add(RecipeItemResponse.builder()
                            .ingredientName(recipeItem.getIngredient().getName())
                            .quantity(recipeItem.getQuantity())
                            .baseUnitCode(recipeItem.getIngredient().getBaseUnitCode())
                            .build());
                }
                productResponses.add(productResponse);
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(productResponses)
                    .totalElements(result.getTotalElements())
                    .totalPages((long) result.getTotalPages())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}
