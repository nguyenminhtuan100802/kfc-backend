package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.IngredientRequest;
import com.codegym.kfcbackend.entity.Ingredient;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IIngredientService {
    Ingredient createIngredient(IngredientRequest request);

    Page<Ingredient> getIngredientByKeyword(String keyword, int page, int size, Long categoryId);
    List<Ingredient> getAllIngredients();

    Ingredient editIngredient(Long id, IngredientRequest request);

    void deleteIngredient(Long id);
}
