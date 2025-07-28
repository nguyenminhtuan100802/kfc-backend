package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.BillRequest;
import com.codegym.kfcbackend.dto.request.SummaryReportRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.BillItemDetailResponse;
import com.codegym.kfcbackend.dto.response.BillItemResponse;
import com.codegym.kfcbackend.dto.response.BillResponse;
import com.codegym.kfcbackend.dto.response.SummaryReportResponse;
import com.codegym.kfcbackend.entity.Bill;
import com.codegym.kfcbackend.entity.BillItem;
import com.codegym.kfcbackend.entity.BillItemDetail;
import com.codegym.kfcbackend.service.IBillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("bills")
public class BillController {
    private final IBillService billService;

    public BillController(IBillService billService) {
        this.billService = billService;
    }

    @PostMapping
    public ResponseEntity<?> createBill(@RequestBody BillRequest request) {
        try {
            Bill bill = billService.createBill(request);
            List<BillItemResponse> billItemResponses = new ArrayList<>();
            for (BillItem billItem : bill.getBillItems()) {
                if (billItem.getCombo() != null) {
                    billItemResponses.add(BillItemResponse.builder()
                            .comboNameOrProductName(billItem.getCombo().getName())
                            .quantity(billItem.getQuantity())
                            .totalPrice(billItem.getTotalPrice())
                            .build());
                }
                if (billItem.getProduct() != null) {
                    billItemResponses.add(BillItemResponse.builder()
                            .comboNameOrProductName(billItem.getProduct().getName())
                            .quantity(billItem.getQuantity())
                            .totalPrice(billItem.getTotalPrice())
                            .build());
                }
            }
            BillResponse billResponse = BillResponse.builder()
                    .billDate(bill.getBillDate())
                    .totalPrice(bill.getTotalRevenue())
                    .staffName(bill.getStaff().getUsername())
                    .billItems(billItemResponses)
                    .build();
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(billResponse)
                    .message("Bill created successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBills() {
        try {
                List<Bill> bills = billService.getAllBills();
            List<BillResponse> billResponses = new ArrayList<>();
            for (Bill bill : bills) {
                BillResponse billResponse = BillResponse.builder()
                        .id(bill.getId())
                        .billDate(bill.getBillDate())
                        .totalPrice(bill.getTotalRevenue())
                        .totalCost(bill.getTotalCost())
                        .status(bill.getStatus())
                        .staffName(bill.getStaff().getUsername())
                        .billItems(new ArrayList<>())
                        .build();

                for (BillItem billItem : bill.getBillItems()) {
                    BillItemResponse billItemResponse = BillItemResponse.builder()
                            .quantity(billItem.getQuantity())
                            .totalPrice(billItem.getTotalPrice())
                            .totalCost(billItem.getTotalCost())
                            .unitPrice(billItem.getTotalPrice().divide(BigDecimal.valueOf(billItem.getQuantity()), 2, RoundingMode.HALF_UP))
                            .unitCost(billItem.getTotalCost().divide(BigDecimal.valueOf(billItem.getQuantity()), 2, RoundingMode.HALF_UP))
                            .billItemDetails(new ArrayList<>())
                            .build();
                    if (billItem.getCombo() != null) {
                        billItemResponse.setComboNameOrProductName(billItem.getCombo().getName());
                        billItemResponse.setDescription(billItem.getCombo().getDescription());
                    }
                    if (billItem.getProduct() != null) {
                        billItemResponse.setComboNameOrProductName(billItem.getProduct().getName());
                        billItemResponse.setDescription(billItem.getProduct().getDescription());
                    }

                    for (BillItemDetail billItemDetail : billItem.getBillItemDetails()) {
                        BillItemDetailResponse billItemDetailResponse = BillItemDetailResponse.builder()
                                .ingredientName(billItemDetail.getIngredientName())
                                .usedQuantity(billItemDetail.getUsedQuantity())
                                .unitCost(billItemDetail.getUnitCost())
                                .baseUnitCode(billItemDetail.getBaseUnitCode())
                                .totalCost(billItemDetail.getTotalCost())
                                .build();
                        billItemResponse.getBillItemDetails().add(billItemDetailResponse);
                    }
                    billResponse.getBillItems().add(billItemResponse);
                }
                billResponses.add(billResponse);
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(billResponses)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping(value = "summary-report")
    public ResponseEntity<?> getSummaryReport(@RequestBody SummaryReportRequest request) {
        try {
            SummaryReportResponse response = billService.getBillsBetween(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(response)
                    .build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> cancelBill(@PathVariable Long id) {
        try {
            billService.cancelBill(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Delete bill successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}
