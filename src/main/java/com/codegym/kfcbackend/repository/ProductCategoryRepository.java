package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    @Query("SELECT c FROM ProductCategory c WHERE c.name = :name")
    Optional<ProductCategory> findByName(@Param("name") String name);
}

