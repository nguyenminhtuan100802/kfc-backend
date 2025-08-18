package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.entity.BillItem;
import com.codegym.kfcbackend.repository.BillItemRepository;
import com.codegym.kfcbackend.service.IBillItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillItemService implements IBillItemService {
    private final BillItemRepository billItemRepository;

    public BillItemService(BillItemRepository billItemRepository) {
        this.billItemRepository = billItemRepository;
    }

    @Override
    public List<BillItem> getAllBillItemsByBillId(Long billId) {
        List<BillItem> billItems = billItemRepository.findAllByBillId(billId);
        return billItems;
    }
}
