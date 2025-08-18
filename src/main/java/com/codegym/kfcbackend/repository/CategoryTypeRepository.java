package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryTypeRepository extends JpaRepository<CategoryType, Long> {
    @Query("SELECT c FROM CategoryType c WHERE c.name = :name")
    Optional<CategoryType> findByName(@Param("name") String name);
}
