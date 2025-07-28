package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.IngredientRequest;
import com.codegym.kfcbackend.entity.Ingredient;
import com.codegym.kfcbackend.entity.IngredientCategory;
import com.codegym.kfcbackend.entity.UnitOfMeasure;
import com.codegym.kfcbackend.repository.IngredientCategoryRepository;
import com.codegym.kfcbackend.repository.IngredientRepository;
import com.codegym.kfcbackend.repository.UnitOfMeasureRepository;
import com.codegym.kfcbackend.service.IIngredientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class IngredientService implements IIngredientService {
    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;

    public IngredientService(IngredientRepository ingredientRepository,
                             UnitOfMeasureRepository unitOfMeasureRepository,
                             IngredientCategoryRepository ingredientCategoryRepository) {
        this.ingredientRepository = ingredientRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientCategoryRepository = ingredientCategoryRepository;
    }

    @Override
    public Ingredient createIngredient(IngredientRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        Ingredient existingIngredient = ingredientRepository.findByName(request.getName()).orElse(null);
        if (existingIngredient != null) {
            throw new RuntimeException(String.format(AppConstants.INGREDIENT_ALREADY_EXISTS, request.getName()));
        }

        IngredientCategory existingIngredientCategory = ingredientCategoryRepository.findByName(request.getIngredientCategoryName())
                .orElseThrow(() -> new RuntimeException("Ingredient category not found: " + request.getIngredientCategoryName()));

        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository.findByBaseUnitCode(request.getBaseUnitCode())
                .stream().findFirst().orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.UNIT_OF_MEASURE_NOT_FOUND, request.getBaseUnitCode())));

        Ingredient ingredient = Ingredient.builder()
                .name(request.getName())
                .averageCost(BigDecimal.ZERO)
                .currentQuantity(BigDecimal.ZERO)
                .baseUnitCode(existingUnitOfMeasure.getBaseUnitCode())
                .ingredientCategory(existingIngredientCategory)
                .build();

        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return savedIngredient;
    }

    @Override
    public Page<Ingredient> getIngredientByKeyword(String keyword, int page, int size, Long categoryId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ingredient> ingredients = ingredientRepository.searchByKeywordAndCategory(keyword, categoryId, pageable);
        return ingredients;
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients;
    }

    @Override
    public Ingredient editIngredient(Long id, IngredientRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        Ingredient checkIngredientName = ingredientRepository.findByName(request.getName()).orElse(null);
        if (checkIngredientName != null && !checkIngredientName.getId().equals(id)) {
            throw new RuntimeException(String.format(AppConstants.INGREDIENT_ALREADY_EXISTS, request.getName()));
        }

        IngredientCategory existingIngredientCategory = ingredientCategoryRepository.findByName(request.getIngredientCategoryName())
                .orElseThrow(() -> new RuntimeException("Ingredient category not found: " + request.getIngredientCategoryName()));

        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository.findByBaseUnitCode(request.getBaseUnitCode())
                .stream().findFirst().orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.UNIT_OF_MEASURE_NOT_FOUND, request.getBaseUnitCode())));

        Ingredient updatedIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(AppConstants.INGREDIENT_NOT_FOUND_WITH_ID, id)));

        updatedIngredient.setName(request.getName());
        updatedIngredient.setBaseUnitCode(existingUnitOfMeasure.getBaseUnitCode());
        updatedIngredient.setIngredientCategory(existingIngredientCategory);

        Ingredient savedIngredient = ingredientRepository.save(updatedIngredient);
        return savedIngredient;
    }

    @Override
    public void deleteIngredient(Long id) {
        Ingredient existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(AppConstants.INGREDIENT_NOT_FOUND_WITH_ID, id)));
        ingredientRepository.delete(existingIngredient);
    }
}
