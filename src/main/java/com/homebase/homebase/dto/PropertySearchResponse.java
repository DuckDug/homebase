package com.homebase.homebase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertySearchResponse {

    private Long id;
    private String rentcastId;
    private String formattedAddress;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
    private String county;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String propertyType;
    private Integer bedrooms;
    private BigDecimal bathrooms;
    private Integer squareFootage;
    private Integer lotSize;
    private Integer yearBuilt;
    private LocalDateTime lastSaleDate;
    private Long lastSalePrice;
    private BigDecimal hoaFee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
