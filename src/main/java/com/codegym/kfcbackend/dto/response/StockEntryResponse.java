package com.codegym.kfcbackend.dto.response;

import jakarta.persistence.Column;
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
public class StockEntryResponse {
    private Long id;
    private BigDecimal quantity;
    private String baseUnitCode;
    private BigDecimal pricePerUnit;
    private BigDecimal totalPrice;
    private LocalDateTime importedAt;
    private String ingredientName;
    private boolean finalized;
}
