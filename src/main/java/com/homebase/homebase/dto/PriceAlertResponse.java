package com.homebase.homebase.dto;

import com.homebase.homebase.model.PriceAlertCondition;
import com.homebase.homebase.model.PriceAlertStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PriceAlertResponse {

    private Long id;
    private String symbol;
    private BigDecimal targetPrice;
    private PriceAlertCondition condition;
    private PriceAlertStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
