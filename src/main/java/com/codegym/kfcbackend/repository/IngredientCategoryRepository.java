package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.IngredientCategory;
import com.codegym.kfcbackend.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {
    @Query("SELECT c FROM IngredientCategory c WHERE c.name = :name")
    Optional<IngredientCategory> findByName(@Param("name") String name);

    @Query("SELECT c FROM IngredientCategory c JOIN c.ingredients i WHERE i.name = :ingredientName")
    Optional<IngredientCategory> findByIngredientName(@Param("ingredientName") String ingredientName);
}

