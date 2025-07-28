package com.codegym.kfcbackend.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillItemDetailResponse {
    private String ingredientName;
    private BigDecimal unitCost;
    private String baseUnitCode;
    private BigDecimal totalCost;
    private BigDecimal usedQuantity;
}
