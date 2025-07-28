package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.UnitOfMeasure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, Long> {
    @Query("SELECT u FROM UnitOfMeasure u WHERE u.code = :code")
    Optional<UnitOfMeasure> findByCodename(@Param("code") String code);

    @Query("SELECT u FROM UnitOfMeasure u WHERE u.baseUnitCode = :baseUnitCode")
    List<UnitOfMeasure> findByBaseUnitCode(@Param("baseUnitCode") String baseUnitCode);

    @Query("""
                SELECT u 
                FROM UnitOfMeasure u
                WHERE u.code = :code
                  AND u.baseUnitCode = :baseUnitCode
            """)
    Optional<UnitOfMeasure> findByCodeAndBaseUnitCode(
            @Param("code") String code,
            @Param("baseUnitCode") String baseUnitCode
    );

    @Query("""
              SELECT u 
              FROM UnitOfMeasure u
              ORDER BY u.baseUnitCode ASC, u.factorToBase DESC
            """)
    List<UnitOfMeasure> findAllGroupByBaseUnitThenFactorDesc();


    @Query("SELECT u FROM UnitOfMeasure u " +
            " WHERE (:kw IS NULL " +
            "    OR LOWER(u.code) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "    OR LOWER(u.baseUnitCode) LIKE LOWER(CONCAT('%', :kw, '%'))) " +
            " ORDER BY u.baseUnitCode ASC, u.factorToBase DESC")
    Page<UnitOfMeasure> findAllByKeyword(@Param("kw") String keyword, Pageable pageable);
}
