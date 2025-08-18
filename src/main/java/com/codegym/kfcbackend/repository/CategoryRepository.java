package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.categoryType.id = :categoryTypeId")
    List<Category> findByCategoryTypeId(@Param("categoryTypeId") Long categoryTypeId);

    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Optional<Category> findByName(@Param("name") String name);
}
