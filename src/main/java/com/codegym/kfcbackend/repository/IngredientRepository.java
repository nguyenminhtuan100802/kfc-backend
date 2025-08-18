package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Query("SELECT i FROM Ingredient i WHERE i.name = :name")
    Optional<Ingredient> findByName(@Param("name") String name);

    @Query("""
        SELECT i
        FROM Ingredient i
        WHERE (:keyword IS NULL
               OR LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:categoryId IS NULL
               OR i.ingredientCategory.id = :categoryId)
        """)
    Page<Ingredient> searchByKeywordAndCategory(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    @Query("SELECT COUNT(i) FROM Ingredient i WHERE i.ingredientCategory.id = :ingredientCategoryId")
    long countByCategoryId(@Param("ingredientCategoryId") Long ingredientCategoryId);
}
