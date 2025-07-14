package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.ComboItemRequest;
import com.codegym.kfcbackend.dto.request.ComboRequest;
import com.codegym.kfcbackend.dto.request.ProductRequest;
import com.codegym.kfcbackend.dto.request.RecipeItemRequest;
import com.codegym.kfcbackend.entity.Category;
import com.codegym.kfcbackend.entity.Combo;
import com.codegym.kfcbackend.entity.ComboItem;
import com.codegym.kfcbackend.entity.Ingredient;
import com.codegym.kfcbackend.entity.Product;
import com.codegym.kfcbackend.entity.RecipeItem;
import com.codegym.kfcbackend.entity.UnitOfMeasure;
import com.codegym.kfcbackend.repository.CategoryRepository;
import com.codegym.kfcbackend.repository.ComboRepository;
import com.codegym.kfcbackend.repository.IngredientRepository;
import com.codegym.kfcbackend.repository.ProductRepository;
import com.codegym.kfcbackend.repository.UnitOfMeasureRepository;
import com.codegym.kfcbackend.service.IProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ComboRepository comboRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          ComboRepository comboRepository,
                          IngredientRepository ingredientRepository,
                          UnitOfMeasureRepository unitOfMeasureRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.comboRepository = comboRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
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
        Category existingCategory = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.CATEGORY_NOT_FOUND,
                                request.getCategoryName())));

        Product existingProduct = productRepository.findByName(request.getName()).orElse(null);
        if (existingProduct != null) {
            throw new RuntimeException(String.format(AppConstants.PRODUCT_ALREADY_EXISTS, request.getName()));
        }

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .category(existingCategory)
                .build();

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
                    .product(product)
                    .build();
            product.getRecipeItems().add(recipeItem);
        }

        Product savedProduct = productRepository.save(product);
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
        Category existingCategory = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.CATEGORY_NOT_FOUND,
                                request.getCategoryName())));

        Product checkExistingProductName = productRepository.findByName(request.getName()).orElse(null);
        if (checkExistingProductName != null && !checkExistingProductName.getId().equals(id) ) {
            throw new RuntimeException(String.format(AppConstants.PRODUCT_ALREADY_EXISTS, request.getName()));
        }
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setCategory(existingCategory);
        existingProduct.getRecipeItems().clear();

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

            existingProduct.getRecipeItems().add(recipeItem);
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
    @Transactional
    public Combo createCombo(ComboRequest request) {
        Category existingCategory = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.CATEGORY_NOT_FOUND,
                                request.getCategoryName())));

        Combo existingCombo = comboRepository.findByName(request.getName()).orElse(null);
        if (existingCombo != null) {
            throw new RuntimeException(String.format(AppConstants.COMBO_ALREADY_EXISTS,
                    request.getName()));
        }

        if (request.getDescription() == null ||
                request.getDescription().trim().isEmpty() ||
                request.getImageUrl() == null ||
                request.getImageUrl().trim().isEmpty()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }

        LocalDate startDate = request.getDiscountStartDate();
        LocalDate endDate = request.getDiscountEndDate();
        LocalTime startTime = request.getDiscountStartTime();
        LocalTime endTime = request.getDiscountEndTime();
        if (startDate != null && endDate != null && startTime != null && endTime != null) {
            if (startDate.isAfter(endDate)) {
                throw new RuntimeException(String.format(
                        AppConstants.COMBO_DATE_ERROR, startDate, endDate));
            }
            if (startTime.isAfter(endTime)) {
                throw new RuntimeException(String.format(
                        AppConstants.COMBO_TIME_ERROR, startTime, endTime));
            }
        } else {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        Combo newCombo = new Combo();
        List<ComboItem> comboItems = new ArrayList<>();
        for (ComboItemRequest comboItemRequest : request.getComboItems()) {
            if (comboItemRequest.getQuantity() <= 0) {
                throw new RuntimeException(AppConstants.PRODUCT_QUANTITY_ERROR);
            }
            Product existingProduct = productRepository.findByName(comboItemRequest.getProductName())
                    .orElseThrow(() -> new RuntimeException(String.format(
                            AppConstants.PRODUCT_NOT_FOUND, comboItemRequest.getProductName())));

            BigDecimal quantity = BigDecimal.valueOf(comboItemRequest.getQuantity());
            BigDecimal itemTotalPrice = existingProduct.getPrice().multiply(quantity);
            totalPrice = totalPrice.add(itemTotalPrice);

            ComboItem comboItem = ComboItem.builder()
                    .product(existingProduct)
                    .combo(newCombo)
                    .quantity(comboItemRequest.getQuantity())
                    .build();
            comboItems.add(comboItem);
        }
        if (request.getDiscountAmount() != null &&
                request.getDiscountAmount().compareTo(BigDecimal.ZERO) >= 0) {
            if (request.getDiscountAmount().compareTo(totalPrice) > 0) {
                throw new RuntimeException(AppConstants.DISCOUNT_ERROR);
            }
        } else {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }

        newCombo.setName(request.getName());
        newCombo.setDescription(request.getDescription());
        newCombo.setImageUrl(request.getImageUrl());
        newCombo.setTotalPrice(totalPrice);
        newCombo.setDiscountAmount(request.getDiscountAmount());
        newCombo.setTotalPriceAfterDiscount(totalPrice.subtract(request.getDiscountAmount()));
        newCombo.setDiscountStartDate(startDate);
        newCombo.setDiscountEndDate(endDate);
        newCombo.setDiscountStartTime(startTime);
        newCombo.setDiscountEndTime(endTime);
        newCombo.setCategory(existingCategory);
        newCombo.setComboItems(comboItems);

        Combo savedCombo = comboRepository.save(newCombo);
        return savedCombo;
    }

    @Override
    public List<Combo> getAllCombos() {
        List<Combo> combos = comboRepository.findAll();
        return combos;
    }
}
