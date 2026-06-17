package com.homebase.homebase.client;

import com.homebase.homebase.model.Property;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RealEstateApiClient {

    private final WebClient webClient;

    @Value("${rentcast.api-key}")
    private String apiKey;

    public RealEstateApiClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://api.rentcast.io/v1")
                .build();
    }

    public List<Property> fetchPropertiesByCity(String city, String state, int limit) {
        List<Map<String, Object>> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/properties")
                        .queryParam("city", city)
                        .queryParam("state", state)
                        .queryParam("limit", limit)
                        .build())
                .header("X-Api-Key", apiKey)
                .retrieve()
                .bodyToFlux(Map.class)
                .cast(Map.class)
                .map(m -> (Map<String, Object>) m)
                .collectList()
                .block();

        if (response == null) return new ArrayList<>();

        return response.stream()
                .map(this::mapToProperty)
                .toList();
    }

    private Property mapToProperty(Map<String, Object> data) {
        Property.PropertyBuilder builder = Property.builder()
                .rentcastId(getString(data, "id"))
                .formattedAddress(getString(data, "formattedAddress"))
                .addressLine1(getString(data, "addressLine1"))
                .addressLine2(getString(data, "addressLine2"))
                .city(getString(data, "city"))
                .state(getString(data, "state"))
                .zipCode(getString(data, "zipCode"))
                .county(getString(data, "county"))
                .propertyType(getString(data, "propertyType"))
                .yearBuilt(getInteger(data, "yearBuilt"))
                .bedrooms(getInteger(data, "bedrooms"))
                .squareFootage(getInteger(data, "squareFootage"))
                .lotSize(getInteger(data, "lotSize"))
                .lastSalePrice(getLong(data, "lastSalePrice"));

        // handle decimals
        if (data.get("latitude") != null)
            builder.latitude(new BigDecimal(data.get("latitude").toString()));
        if (data.get("longitude") != null)
            builder.longitude(new BigDecimal(data.get("longitude").toString()));
        if (data.get("bathrooms") != null)
            builder.bathrooms(new BigDecimal(data.get("bathrooms").toString()));

        // handle HOA fee nested object
        if (data.get("hoa") instanceof Map<?, ?> hoa && hoa.get("fee") != null)
            builder.hoaFee(new BigDecimal(hoa.get("fee").toString()));

        // handle last sale date
        String lastSaleDate = getString(data, "lastSaleDate");
        if (lastSaleDate != null)
            builder.lastSaleDate(LocalDateTime.parse(lastSaleDate,
                    DateTimeFormatter.ISO_DATE_TIME));

        // store nested objects as JSONB
        if (data.get("features") instanceof Map<?, ?> f)
            builder.features((Map<String, Object>) f);
        if (data.get("taxAssessments") instanceof Map<?, ?> t)
            builder.taxAssessments((Map<String, Object>) t);
        if (data.get("propertyTaxes") instanceof Map<?, ?> pt)
            builder.propertyTaxes((Map<String, Object>) pt);
        if (data.get("history") instanceof Map<?, ?> h)
            builder.history((Map<String, Object>) h);

        return builder.build();
    }

    // safe helper methods to extract typed values from the response map
    private String getString(Map<String, Object> data, String key) {
        Object val = data.get(key);
        return val != null ? val.toString() : null;
    }

    private Integer getInteger(Map<String, Object> data, String key) {
        Object val = data.get(key);
        if (val == null) return null;
        if (val instanceof Integer i) return i;
        try { return Integer.parseInt(val.toString()); }
        catch (NumberFormatException e) { return null; }
    }

    private Long getLong(Map<String, Object> data, String key) {
        Object val = data.get(key);
        if (val == null) return null;
        if (val instanceof Long l) return l;
        try { return Long.parseLong(val.toString()); }
        catch (NumberFormatException e) { return null; }
    }
}