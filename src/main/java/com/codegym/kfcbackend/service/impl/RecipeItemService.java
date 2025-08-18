package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.entity.RecipeItem;
import com.codegym.kfcbackend.repository.RecipeItemRepository;
import com.codegym.kfcbackend.service.IRecipeItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeItemService implements IRecipeItemService {
    private final RecipeItemRepository recipeItemRepository;

    public RecipeItemService(RecipeItemRepository recipeItemRepository) {
        this.recipeItemRepository = recipeItemRepository;
    }

    @Override
    public List<RecipeItem> getAllRecipeItemsByProductId(Long productId) {
        List<RecipeItem> recipeItems = recipeItemRepository.findAllByProductId(productId);
        return recipeItems;
    }
}
