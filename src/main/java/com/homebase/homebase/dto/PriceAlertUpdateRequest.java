package com.homebase.homebase.dto;

import com.homebase.homebase.model.PriceAlertCondition;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PriceAlertUpdateRequest {

    private BigDecimal targetPrice;
    private PriceAlertCondition condition;
}
