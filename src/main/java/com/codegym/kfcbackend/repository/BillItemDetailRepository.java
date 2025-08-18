package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.BillItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillItemDetailRepository extends JpaRepository<BillItemDetail, Long> {
    @Query("SELECT b FROM BillItemDetail b WHERE b.billItem.id = :billItemId")
    List<BillItemDetail> findAllByBillItemId(@Param("billItemId") Long billItemId);
}
