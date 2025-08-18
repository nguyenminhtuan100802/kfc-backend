package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.entity.BillItem;

import java.util.List;

public interface IBillItemService {
    List<BillItem> getAllBillItemsByBillId(Long billId);
}
