package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.IngredientRequest;
import com.codegym.kfcbackend.entity.Ingredient;

import java.util.List;

public interface IIngredientService {
    Ingredient createIngredient(IngredientRequest request);
    List<Ingredient> getAllIngredients();
    Ingredient editIngredient(Long id, IngredientRequest request);
    void deleteIngredient(Long id);
}
