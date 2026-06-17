package com.homebase.homebase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "properties")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rentcast_id", nullable = false, unique = true)
    private String rentcastId;

    @Column(name = "formatted_address", nullable = false)
    private String formattedAddress;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state", length = 2)
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "county")
    private String county;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "property_type")
    private String propertyType;

    @Column(name = "bedrooms")
    private Integer bedrooms;

    @Column(name = "bathrooms", precision = 4, scale = 1)
    private BigDecimal bathrooms;

    @Column(name = "square_footage")
    private Integer squareFootage;

    @Column(name = "lot_size")
    private Integer lotSize;

    @Column(name = "year_built")
    private Integer yearBuilt;

    @Column(name = "last_sale_date")
    private LocalDateTime lastSaleDate;

    @Column(name = "last_sale_price")
    private Long lastSalePrice;

    @Column(name = "hoa_fee", precision = 10, scale = 2)
    private BigDecimal hoaFee;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "features", columnDefinition = "jsonb")
    private Map<String, Object> features;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tax_assessments", columnDefinition = "jsonb")
    private Map<String, Object> taxAssessments;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "property_taxes", columnDefinition = "jsonb")
    private Map<String, Object> propertyTaxes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "history", columnDefinition = "jsonb")
    private Map<String, Object> history;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}