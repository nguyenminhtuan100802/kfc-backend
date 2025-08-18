package com.codegym.kfcbackend.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
public class UnitOfMeasureRequest {
    @NotBlank(message = "Code must not be blank")
    private String code;

    @NotBlank(message = "BaseUnitCode must not be blank")
    private String baseUnitCode;

    @Positive(message = "FactorToBase must be greater than 0")
    private BigDecimal factorToBase;
}
