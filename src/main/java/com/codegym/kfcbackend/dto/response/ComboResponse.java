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
public class ComboResponse {
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal discountAmount;
    private BigDecimal totalPrice;
    private BigDecimal totalPriceAfterDiscount;
}
