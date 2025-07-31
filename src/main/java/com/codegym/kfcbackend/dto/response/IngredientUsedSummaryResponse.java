package com.codegym.kfcbackend.dto.response;

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
public class IngredientUsedSummaryResponse {
    private String ingredientName;
    private String ingredientCategoryName;
    private BigDecimal quantityUsed;
    private BigDecimal quantityRemaining;
    private String baseUnitCode;
}
