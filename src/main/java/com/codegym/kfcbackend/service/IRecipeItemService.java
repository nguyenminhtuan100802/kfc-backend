package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.entity.RecipeItem;

import java.util.List;

public interface IRecipeItemService {
    List<RecipeItem> getAllRecipeItemsByProductId(Long productId);
}
