package com.homebase.homebase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertySearchRequest {

    private String city;
    private String state;
    private Integer bedrooms;
    private String propertyType;
}
