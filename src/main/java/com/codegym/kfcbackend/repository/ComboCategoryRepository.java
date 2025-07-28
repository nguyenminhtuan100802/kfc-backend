package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.ComboCategory;
import com.codegym.kfcbackend.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ComboCategoryRepository extends JpaRepository<ComboCategory, Long> {
    @Query("SELECT c FROM ComboCategory c WHERE c.name = :name")
    Optional<ComboCategory> findByName(@Param("name") String name);
}

