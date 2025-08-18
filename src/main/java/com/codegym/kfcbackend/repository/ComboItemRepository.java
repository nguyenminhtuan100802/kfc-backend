package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.ComboItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComboItemRepository extends JpaRepository<ComboItem, Long> {
    @Query("SELECT ci FROM ComboItem ci WHERE ci.combo.id = :comboId")
    List<ComboItem> findAllByComboId(@Param("comboId") Long comboId);

    @Modifying
    @Query("DELETE FROM ComboItem ci WHERE ci.combo.id = :comboId")
    void deleteByComboId(@Param("comboId") Long comboId);
}
