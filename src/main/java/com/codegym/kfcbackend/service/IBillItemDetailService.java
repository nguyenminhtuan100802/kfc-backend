package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.entity.BillItemDetail;

import java.util.List;

public interface IBillItemDetailService {
    List<BillItemDetail> getAllBillItemDetailsByBillItemId(Long billItemId);
}
