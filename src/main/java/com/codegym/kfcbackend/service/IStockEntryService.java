package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.StockEntryRequest;
import com.codegym.kfcbackend.entity.StockEntry;

import java.util.List;

public interface IStockEntryService {
    StockEntry createStockEntry(StockEntryRequest request);
    StockEntry approveStockEntry(Long id, StockEntryRequest request);
    List<StockEntry> getAllStockEntries();
    StockEntry getStockEntryById(Long id);
    StockEntry updateStockEntry(Long id, StockEntryRequest request);
    void deleteStockEntry(Long id);
}
