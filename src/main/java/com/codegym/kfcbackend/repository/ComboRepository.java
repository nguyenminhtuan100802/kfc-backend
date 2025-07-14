package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.Category;
import com.codegym.kfcbackend.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ComboRepository extends JpaRepository<Combo, Long> {
    @Query("SELECT c FROM Combo c WHERE c.name = :name")
    Optional<Combo> findByName(@Param("name") String name);
}
