package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.ComboRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.ComboItemResponse;
import com.codegym.kfcbackend.dto.response.ComboResponse;
import com.codegym.kfcbackend.entity.Combo;
import com.codegym.kfcbackend.entity.ComboItem;
import com.codegym.kfcbackend.service.IComboItemService;
import com.codegym.kfcbackend.service.IComboService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("combos")
public class ComboController {
    private final IComboService comboService;
    private final IComboItemService comboItemService;

    public ComboController(IComboService comboService,
                           IComboItemService comboItemService) {
        this.comboService = comboService;
        this.comboItemService = comboItemService;
    }

    @PostMapping
    public ResponseEntity<?> createCombo(@RequestBody ComboRequest request) {
        try {
            Combo combo = comboService.createCombo(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message(AppConstants.COMBO_CREATED_SUCCESS)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCombos() {
        try {
            List<Combo> combos = comboService.getAllCombos();

            List<ComboResponse> responses = new ArrayList<>();
            for (Combo combo : combos) {
                ComboResponse comboResponse = ComboResponse.builder()
                        .id(combo.getId())
                        .name(combo.getName())
                        .description(combo.getDescription())
                        .imageUrl(combo.getImageUrl())
                        .discountAmount(combo.getDiscountAmount())
                        .totalPrice(combo.getTotalPrice())
                        .totalPriceAfterDiscount(combo.getTotalPriceAfterDiscount())
                        .discountStartDate(combo.getDiscountStartDate())
                        .discountEndDate(combo.getDiscountEndDate())
                        .discountStartTime(combo.getDiscountStartTime())
                        .discountEndTime(combo.getDiscountEndTime())
                        .categoryName(combo.getComboCategory().getName())
                        .comboItems(new ArrayList<>())
                        .build();

                List<ComboItem> comboItems = comboItemService.getAllComboItemsByComboId(combo.getId());
                for (ComboItem comboItem : comboItems) {
                    comboResponse.getComboItems().add(ComboItemResponse.builder()
                            .productName(comboItem.getProduct().getName())
                            .quantity(comboItem.getQuantity())
                            .build());
                }
                responses.add(comboResponse);
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(responses)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> editCombo(@PathVariable Long id, @RequestBody ComboRequest request) {
        try {
            Combo combo = comboService.editCombo(id, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Update combo successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteCombo(@PathVariable Long id) {
        try {
            comboService.deleteCombo(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Delete combo successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}
