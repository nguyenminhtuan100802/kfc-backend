package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.StockEntryRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.StockEntryResponse;
import com.codegym.kfcbackend.entity.StockEntry;
import com.codegym.kfcbackend.service.IStockEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("stock-entries")
public class StockEntryController {
    private final IStockEntryService stockEntryService;

    public StockEntryController(IStockEntryService stockEntryService) {
        this.stockEntryService = stockEntryService;
    }

    @PostMapping
    public ResponseEntity<?> createStockEntry(@RequestBody StockEntryRequest request) {
        try {
            StockEntry created = stockEntryService.createStockEntry(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(created)
                    .message("Created stock entry successfully")
                    .build());
        } catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.builder()
                            .message(ex.getMessage())
                            .build());
        }
    }

    @PostMapping(value = "/approve/{id}")
    public ResponseEntity<?> approveStockEntry(@PathVariable Long id, @RequestBody StockEntryRequest request) {
        try {
            StockEntry created = stockEntryService.approveStockEntry(id, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(created)
                    .message("Approved stock entry successfully")
                    .build());

        } catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.builder()
                            .message(ex.getMessage())
                            .build());
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllStockEntries() {
        List<StockEntry> stockEntries = stockEntryService.getAllStockEntries();
        List<StockEntryResponse> responses = new java.util.ArrayList<>();
        for (StockEntry stockEntry : stockEntries) {
            responses.add(StockEntryResponse.builder()
                    .id(stockEntry.getId())
                    .quantity(stockEntry.getQuantity())
                    .baseUnitCode(stockEntry.getBaseUnitCode())
                    .pricePerUnit(stockEntry.getPricePerUnit())
                    .totalPrice(stockEntry.getQuantity().multiply(stockEntry.getPricePerUnit()))
                    .importedAt(stockEntry.getImportedAt())
                    .ingredientName(stockEntry.getIngredient().getName())
                    .finalized(stockEntry.isFinalized())
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .data(responses)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStockEntryById(@PathVariable Long id) {
        try {
            StockEntry entry = stockEntryService.getStockEntryById(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(entry)
                    .build());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                    .message(ex.getMessage())
                    .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStockEntry(
            @PathVariable Long id,
            @RequestBody StockEntryRequest request) {
        try {
            StockEntry updated = stockEntryService.updateStockEntry(id, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(updated)
                    .message("Updated stock entry successfully")
                    .build());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(ex.getMessage())
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStockEntry(@PathVariable Long id) {
        try {
            stockEntryService.deleteStockEntry(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Deleted stock entry with id = " + id)
                    .build());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                    .message(ex.getMessage())
                    .build());
        }
    }
}
