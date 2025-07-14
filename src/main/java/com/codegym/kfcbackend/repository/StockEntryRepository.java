package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.StockEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockEntryRepository extends JpaRepository<StockEntry, Long> {
    @Query("""
              SELECT se
              FROM StockEntry se
              JOIN se.ingredient i
              ORDER BY se.finalized ASC , i.name ASC, se.importedAt DESC
            """)
    List<StockEntry> findAllSortedByIngredientAndDate();
}
