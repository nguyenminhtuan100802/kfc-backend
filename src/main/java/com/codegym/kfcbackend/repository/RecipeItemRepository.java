package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.RecipeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeItemRepository extends JpaRepository<RecipeItem, Long> {
    @Query("SELECT ri FROM RecipeItem ri WHERE ri.product.id = :productId")
    List<RecipeItem> findAllByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("DELETE FROM RecipeItem ri WHERE ri.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("DELETE FROM RecipeItem ri WHERE ri.ingredient.id = :ingredientId")
    void deleteAllByIngredientId(@Param("ingredientId") Long ingredientId);
}
