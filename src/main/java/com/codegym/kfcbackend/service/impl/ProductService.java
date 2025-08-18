package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.ProductRequest;
import com.codegym.kfcbackend.dto.request.RecipeItemRequest;
import com.codegym.kfcbackend.entity.Category;
import com.codegym.kfcbackend.entity.Ingredient;
import com.codegym.kfcbackend.entity.Product;
import com.codegym.kfcbackend.entity.RecipeItem;
import com.codegym.kfcbackend.entity.UnitOfMeasure;
import com.codegym.kfcbackend.repository.CategoryRepository;
import com.codegym.kfcbackend.repository.IngredientRepository;
import com.codegym.kfcbackend.repository.ProductRepository;
import com.codegym.kfcbackend.repository.RecipeItemRepository;
import com.codegym.kfcbackend.repository.UnitOfMeasureRepository;
import com.codegym.kfcbackend.service.IProductService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeItemRepository recipeItemRepository;

    public ProductService(ProductRepository productRepository,
                          IngredientRepository ingredientRepository,
                          UnitOfMeasureRepository unitOfMeasureRepository,
                          CategoryRepository categoryRepository,
                          RecipeItemRepository recipeItemRepository) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.categoryRepository = categoryRepository;
        this.recipeItemRepository = recipeItemRepository;
    }

    @Override
    @Transactional
    public Product createProduct(ProductRequest request) {
        if (request.getDescription() == null || request.getDescription().trim().isBlank() ||
                request.getImageUrl() == null || request.getImageUrl().trim().isBlank() ||
                request.getName() == null || request.getName().trim().isBlank() ||
                request.getRecipeItems().isEmpty()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(AppConstants.PRODUCT_PRICE_ERROR);
        }
        Category existingProductCategory = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.CATEGORY_NOT_FOUND,
                                request.getCategoryName())));

        Product existingProduct = productRepository.findByName(request.getName()).orElse(null);
        if (existingProduct != null) {
            throw new RuntimeException(String.format(AppConstants.PRODUCT_ALREADY_EXISTS, request.getName()));
        }

        Product savedProduct = productRepository.save(Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .productCategory(existingProductCategory)
                .build());

        for (RecipeItemRequest recipeItemRequest : request.getRecipeItems()) {
            if (recipeItemRequest.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Recipe item quantity must be > 0");
            }
            Ingredient existingIngredient = ingredientRepository.findByName(recipeItemRequest.getIngredientName())
                    .orElseThrow(() -> new RuntimeException(String.format("Ingredient with name %s not found",
                            recipeItemRequest.getIngredientName())));

            UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository.findByBaseUnitCode(recipeItemRequest.getBaseUnitCode())
                    .stream().findFirst().orElseThrow(() -> new RuntimeException(String.format("Unit of measure %s not found",
                            recipeItemRequest.getBaseUnitCode())));

            RecipeItem recipeItem = RecipeItem.builder()
                    .quantity(recipeItemRequest.getQuantity())
                    .baseUnitCode(existingUnitOfMeasure.getBaseUnitCode())
                    .ingredient(existingIngredient)
                    .product(savedProduct)
                    .build();
            RecipeItem savedRecipeItem = recipeItemRepository.save(recipeItem);
        }

        return savedProduct;
    }

    @Override
    @Transactional
    public Product editProduct(Long id, ProductRequest request) {
        if (request.getDescription() == null || request.getDescription().trim().isBlank() ||
                request.getImageUrl() == null || request.getImageUrl().trim().isBlank() ||
                request.getName() == null || request.getName().trim().isBlank() ||
                request.getRecipeItems().isEmpty()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(AppConstants.PRODUCT_PRICE_ERROR);
        }
        Category existingProductCategory = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.CATEGORY_NOT_FOUND,
                                request.getCategoryName())));

        Product existingProduct = productRepository.findByName(request.getName()).
                orElseThrow(() -> new RuntimeException("Product with name " + request.getName() + " not found"));
        if (!existingProduct.getId().equals(id)) {
            throw new RuntimeException(String.format(AppConstants.PRODUCT_ALREADY_EXISTS, request.getName()));
        }

        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setProductCategory(existingProductCategory);
        recipeItemRepository.deleteByProductId(existingProduct.getId());

        for (RecipeItemRequest recipeItemRequest : request.getRecipeItems()) {
            if (recipeItemRequest.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Recipe item quantity must be > 0");
            }
            Ingredient existingIngredient = ingredientRepository.findByName(recipeItemRequest.getIngredientName())
                    .orElseThrow(() -> new RuntimeException(String.format("Ingredient with name %s not found",
                            recipeItemRequest.getIngredientName())));

            UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository.findByBaseUnitCode(recipeItemRequest.getBaseUnitCode())
                    .stream().findFirst().orElseThrow(() -> new RuntimeException(String.format("Unit of measure %s not found",
                            recipeItemRequest.getBaseUnitCode())));

            RecipeItem recipeItem = RecipeItem.builder()
                    .quantity(recipeItemRequest.getQuantity())
                    .baseUnitCode(existingUnitOfMeasure.getBaseUnitCode())
                    .ingredient(existingIngredient)
                    .product(existingProduct)
                    .build();

            RecipeItem savedRecipeItem = recipeItemRepository.save(recipeItem);
        }
        Product savedProduct = productRepository.save(existingProduct);
        return savedProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
        productRepository.delete(existingProduct);
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    public Page<Product> getProductByKeyword(String keyword, int page, int size, Long productCategoryId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.searchByKeywordAndCategory(keyword, productCategoryId, pageable);
        return products;
    }
}
