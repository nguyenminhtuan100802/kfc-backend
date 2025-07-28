package com.codegym.kfcbackend.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComboResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal discountAmount;
    private BigDecimal totalPrice;
    private BigDecimal totalPriceAfterDiscount;
    private LocalDate discountStartDate;
    private LocalDate discountEndDate;
    private LocalTime discountStartTime;
    private LocalTime discountEndTime;
    private String categoryName;
    private List<ComboItemResponse> comboItems;
}
