package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.Ingredient;
import com.codegym.kfcbackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Optional<Product> findByName(@Param("name") String name);

    @Query("""
        SELECT p
        FROM Product p
        WHERE (:keyword IS NULL
               OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:productCategoryId IS NULL
               OR p.productCategory.id = :productCategoryId)
        """)
    Page<Product> searchByKeywordAndCategory(
            @Param("keyword") String keyword,
            @Param("productCategoryId") Long productCategoryId,
            Pageable pageable
    );
}
