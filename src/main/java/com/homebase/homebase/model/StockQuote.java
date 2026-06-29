package com.homebase.homebase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_quotes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockQuote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String symbol;

    private String name;

    private BigDecimal price;

    private BigDecimal open;

    private BigDecimal high;

    private BigDecimal low;

    @Column(name = "previous_close")
    private BigDecimal previousClose;

    private Long volume;

    @Column(name = "quote_date", nullable = false)
    private LocalDate quoteDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
