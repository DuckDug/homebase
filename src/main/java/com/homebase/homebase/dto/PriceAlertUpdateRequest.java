package com.homebase.homebase.dto;

import com.homebase.homebase.model.PriceAlertCondition;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PriceAlertUpdateRequest {

    @DecimalMin(value = "0.01", message = "Target Price must be greater than zero.")
    @Digits(integer = 10, fraction = 2, message = "Target Price must have at most 10 whole digits and 2 decimal places.")
    private BigDecimal targetPrice;

    private PriceAlertCondition condition;

    @AssertTrue(message = "At least one field must be provided")
    private boolean isAtLeastOneFieldPresent() {
        return targetPrice != null || condition != null;
    }
}
