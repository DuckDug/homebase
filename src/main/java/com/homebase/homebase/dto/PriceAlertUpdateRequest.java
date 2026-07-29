package com.homebase.homebase.dto;

import com.homebase.homebase.model.PriceAlertCondition;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PriceAlertUpdateRequest {

    private BigDecimal targetPrice;
    private PriceAlertCondition condition;

    @AssertTrue(message = "At least one field must be provided")
    private boolean isAtLeastOneFieldPresent() {
        return targetPrice != null || condition != null;
    }
}
