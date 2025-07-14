package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.dto.request.StockEntryRequest;
import com.codegym.kfcbackend.entity.Ingredient;
import com.codegym.kfcbackend.entity.StockEntry;
import com.codegym.kfcbackend.entity.UnitOfMeasure;
import com.codegym.kfcbackend.repository.IngredientRepository;
import com.codegym.kfcbackend.repository.StockEntryRepository;
import com.codegym.kfcbackend.repository.UnitOfMeasureRepository;
import com.codegym.kfcbackend.service.IStockEntryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StockEntryService implements IStockEntryService {
    private final StockEntryRepository stockEntryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientRepository ingredientRepository;

    public StockEntryService(StockEntryRepository stockEntryRepository,
                             UnitOfMeasureRepository unitOfMeasureRepository,
                             IngredientRepository ingredientRepository) {
        this.stockEntryRepository = stockEntryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional
    public StockEntry createStockEntry(StockEntryRequest request) {
        if (request.getQuantity().compareTo(BigDecimal.ZERO) <= 0 || request.getPricePerUnit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Quantity or Price per unit must be > 0");
        }
        Ingredient exisingIngredient = ingredientRepository.findByName(request.getIngredientName())
                .orElseThrow(() -> new RuntimeException(String.format("Ingredient %s not found", request.getIngredientName())));

        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository
                .findByCodeAndBaseUnitCode(request.getBaseUnitCode(), exisingIngredient.getBaseUnitCode())
                .orElseThrow(() -> new RuntimeException(String.format("Unit of measure %s not found", request.getBaseUnitCode())));

        StockEntry stockEntry = StockEntry.builder()
                .quantity(request.getQuantity())
                .pricePerUnit(request.getPricePerUnit())
                .importedAt(request.getImportedAt())
                .ingredient(exisingIngredient)
                .baseUnitCode(existingUnitOfMeasure.getCode())
                .finalized(false)
                .build();
        StockEntry savedStockEntry = stockEntryRepository.save(stockEntry);
        return savedStockEntry;
    }

    @Override
    @Transactional
    public StockEntry approveStockEntry(Long id, StockEntryRequest request) {
        if (request.getQuantity().compareTo(BigDecimal.ZERO) <= 0 || request.getPricePerUnit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Quantity or Price per unit must be > 0");
        }
        if (request.getImportedAt() == null) {
            throw new RuntimeException("Imported date must be provided");
        }
        Ingredient exisingIngredient = ingredientRepository.findByName(request.getIngredientName())
                .orElseThrow(() -> new RuntimeException(String.format("Ingredient %s not found", request.getIngredientName())));

        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository
                .findByCodeAndBaseUnitCode(request.getBaseUnitCode(), exisingIngredient.getBaseUnitCode())
                .orElseThrow(() -> new RuntimeException(String.format("Unit of measure %s not found", request.getBaseUnitCode())));

        StockEntry existingStockEntry = stockEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Stock entry %d not found", id)));

        exisingIngredient.setCurrentQuantity(exisingIngredient.getCurrentQuantity().add(
                existingStockEntry.getQuantity().multiply(existingUnitOfMeasure.getFactorToBase())));
        existingStockEntry.setFinalized(true);
        StockEntry savedStockEntry = stockEntryRepository.save(existingStockEntry);
        return savedStockEntry;
    }

    @Override
    public List<StockEntry> getAllStockEntries() {
        List<StockEntry> stockEntries = stockEntryRepository.findAllSortedByIngredientAndDate();
        return stockEntries;
    }

    @Override
    public StockEntry getStockEntryById(Long id) {
        StockEntry stockEntry = new StockEntry();
        return null;
    }

    @Override
    @Transactional
    public StockEntry updateStockEntry(Long id, StockEntryRequest request) {
        if (request.getQuantity().compareTo(BigDecimal.ZERO) <= 0 || request.getPricePerUnit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Quantity or Price per unit must be > 0");
        }
        if (request.getImportedAt() == null) {
            throw new RuntimeException("Imported date must be provided");
        }
        Ingredient exisingIngredient = ingredientRepository.findByName(request.getIngredientName())
                .orElseThrow(() -> new RuntimeException(String.format("Ingredient %s not found", request.getIngredientName())));

        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository
                .findByCodeAndBaseUnitCode(request.getBaseUnitCode(), exisingIngredient.getBaseUnitCode())
                .orElseThrow(() -> new RuntimeException(String.format("Unit of measure %s not found", request.getBaseUnitCode())));

        StockEntry existingStockEntry = stockEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Stock entry %d not found", id)));

        existingStockEntry.setQuantity(request.getQuantity());
        existingStockEntry.setPricePerUnit(request.getPricePerUnit());
        existingStockEntry.setImportedAt(request.getImportedAt());
        existingStockEntry.setFinalized(false);
        existingStockEntry.setIngredient(exisingIngredient);
        existingStockEntry.setBaseUnitCode(existingUnitOfMeasure.getCode());
        StockEntry savedStockEntry = stockEntryRepository.save(existingStockEntry);
        return savedStockEntry;
    }

    @Override
    public void deleteStockEntry(Long id) {
        StockEntry existingStockEntry = stockEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Stock entry %d not found", id)));
        stockEntryRepository.delete(existingStockEntry);
    }
}
