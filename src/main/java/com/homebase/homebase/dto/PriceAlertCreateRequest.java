package com.homebase.homebase.dto;

import com.homebase.homebase.model.PriceAlertCondition;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PriceAlertCreateRequest {

    @NotBlank(message = "Symbol is Required.")
    @Pattern(regexp = "^[A-Z]{1,5}(\\.[A-Z])?$", message = "Symbol must be 1-5 letters, optionally with a share-class suffix (e.g. BRK.A)")
    private String symbol;

    @NotNull(message = "Target Price is required.")
    @DecimalMin(value = "0.01", message = "Target Price must be greater than zero.")
    @Digits(integer = 10, fraction = 2, message = "Target Price must have at most 10 whole digits and 2 decimal places.")
    private BigDecimal targetPrice;

    @NotNull(message = "Condition is Required.")
    private PriceAlertCondition condition;

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim().toUpperCase();
    }
}