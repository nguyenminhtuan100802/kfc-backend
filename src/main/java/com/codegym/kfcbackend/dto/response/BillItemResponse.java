package com.codegym.kfcbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillItemResponse {
    private String comboNameOrProductName;
    private int quantity;
    private BigDecimal totalPrice;
    private BigDecimal totalCost;
    private BigDecimal unitPrice;
    private BigDecimal unitCost;
    private String description;
    List<BillItemDetailResponse> billItemDetails;
}
