package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {
    @Query("SELECT bi FROM BillItem bi WHERE bi.bill.id = :billId")
    List<BillItem> findAllByBillId(@Param("billId") Long billId);
}
