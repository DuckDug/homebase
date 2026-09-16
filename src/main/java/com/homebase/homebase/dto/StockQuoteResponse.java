package com.homebase.homebase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockQuoteResponse {

    private Long id;
    private String symbol;
    private String name;
    private BigDecimal price;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal previousClose;
    private Long volume;
    private LocalDate quoteDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
