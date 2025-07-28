package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillRepository extends JpaRepository <Bill, Long>{
    @Query("SELECT b FROM Bill b ORDER BY b.billDate DESC")
    List<Bill> findAllBillsOrderByDateDesc();

}
