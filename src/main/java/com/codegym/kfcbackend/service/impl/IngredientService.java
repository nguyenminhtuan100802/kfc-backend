package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.IngredientRequest;
import com.codegym.kfcbackend.entity.Ingredient;
import com.codegym.kfcbackend.entity.UnitOfMeasure;
import com.codegym.kfcbackend.repository.IngredientRepository;
import com.codegym.kfcbackend.repository.UnitOfMeasureRepository;
import com.codegym.kfcbackend.service.IIngredientService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class IngredientService implements IIngredientService {
    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientService(IngredientRepository ingredientRepository,
                             UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientRepository = ingredientRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public Ingredient createIngredient(IngredientRequest request) {
        if (request.getName() == null || request.getName().isBlank() ||
                request.getBaseUnitCode() == null || request.getBaseUnitCode().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        Ingredient existingIngredient = ingredientRepository.findByName(request.getName()).orElse(null);
        if (existingIngredient != null) {
            throw new RuntimeException(String.format(AppConstants.INGREDIENT_ALREADY_EXISTS, request.getName()));
        }
        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository.findByBaseUnitCode(request.getBaseUnitCode())
                .stream().findFirst().orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.UNIT_OF_MEASURE_NOT_FOUND, request.getBaseUnitCode())));

        Ingredient ingredient = Ingredient.builder()
                .name(request.getName())
                .baseUnitCode(existingUnitOfMeasure.getBaseUnitCode())
                .currentQuantity(BigDecimal.ZERO)
                .build();
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return savedIngredient;
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients;
    }

    @Override
    public Ingredient editIngredient(Long id, IngredientRequest request) {
        if (request.getName() == null || request.getName().isBlank() ||
                request.getBaseUnitCode() == null || request.getBaseUnitCode().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        Ingredient checkIngredientName = ingredientRepository.findByName(request.getName()).orElse(null);
        if (checkIngredientName != null && !checkIngredientName.getId().equals(id) ) {
            throw new RuntimeException(String.format(AppConstants.INGREDIENT_ALREADY_EXISTS, request.getName()));
        }
        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository.findByBaseUnitCode(request.getBaseUnitCode())
                .stream().findFirst().orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.UNIT_OF_MEASURE_NOT_FOUND, request.getBaseUnitCode())));

        Ingredient updatedIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(AppConstants.INGREDIENT_NOT_FOUND_WITH_ID, id)));

        updatedIngredient.setName(request.getName());
        updatedIngredient.setBaseUnitCode(existingUnitOfMeasure.getBaseUnitCode());

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
