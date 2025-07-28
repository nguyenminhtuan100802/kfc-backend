package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.StockEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockEntryRepository extends JpaRepository<StockEntry, Long> {
    @Query("""
              SELECT se
              FROM StockEntry se
              JOIN se.ingredient i
              ORDER BY se.finalized ASC , i.name ASC, se.importedAt DESC
            """)
    List<StockEntry> findAllSortedByIngredientAndDate();

    @Query("""
      SELECT s
        FROM StockEntry s
        JOIN s.ingredient i
       WHERE i.name = :ingredientName
         AND s.finalized = true
    """)
    List<StockEntry> findByIngredientNameAndFinalizedTrue(
            @Param("ingredientName") String ingredientName
    );
}
