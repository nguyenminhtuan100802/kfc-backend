package com.codegym.kfcbackend.dto.response;

import com.codegym.kfcbackend.enums.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillResponse {
    private Long id;
    private LocalDateTime billDate;
    private BigDecimal totalPrice;
    private BigDecimal totalCost;
    private BillStatus status;
    private String staffName;
    List<BillItemResponse> billItems;
}
