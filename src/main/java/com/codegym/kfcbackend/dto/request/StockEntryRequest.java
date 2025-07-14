package com.codegym.kfcbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockEntryRequest {
    private BigDecimal quantity;
    private String baseUnitCode;
    private BigDecimal pricePerUnit;
    private LocalDateTime importedAt;
    private String ingredientName;
}
