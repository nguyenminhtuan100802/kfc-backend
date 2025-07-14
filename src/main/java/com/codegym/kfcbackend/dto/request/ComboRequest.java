package com.codegym.kfcbackend.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComboRequest {
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal discountAmount;
    private LocalDate discountStartDate;
    private LocalDate discountEndDate;
    private LocalTime discountStartTime;
    private LocalTime discountEndTime;
    private String categoryName;
    private List<ComboItemRequest> comboItems;
}
