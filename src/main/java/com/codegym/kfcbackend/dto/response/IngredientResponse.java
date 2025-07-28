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
public class IngredientResponse {
    private Long id;
    private String name;
    private String baseUnitCode;
    private BigDecimal averageCost;
    private BigDecimal currentQuantity;
    private String ingredientCategoryName;
}
