package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.entity.BillItemDetail;
import com.codegym.kfcbackend.repository.BillItemDetailRepository;
import com.codegym.kfcbackend.service.IBillItemDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillItemDetailService implements IBillItemDetailService {
    private final BillItemDetailRepository billItemDetailRepository;

    public BillItemDetailService(BillItemDetailRepository billItemDetailRepository) {
        this.billItemDetailRepository = billItemDetailRepository;
    }

    @Override
    public List<BillItemDetail> getAllBillItemDetailsByBillItemId(Long billItemId) {
        List<BillItemDetail> billItemDetails = billItemDetailRepository.findAllByBillItemId(billItemId);
        return billItemDetails;
    }
}
