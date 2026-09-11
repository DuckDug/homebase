package com.homebase.homebase.service;

import com.homebase.homebase.dto.PropertySearchRequest;
import com.homebase.homebase.dto.PropertySearchResponse;
import com.homebase.homebase.model.Property;
import com.homebase.homebase.repository.PropertyRepository;
import com.homebase.homebase.specification.PropertySpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Page<PropertySearchResponse> findProperties(Pageable pageable, PropertySearchRequest request) {
        List<Specification<Property>> specs = Arrays.asList(
                PropertySpecifications.hasCity(request.getCity()),
                PropertySpecifications.hasState(request.getState()),
                PropertySpecifications.hasPropertyType(request.getPropertyType()),
                PropertySpecifications.hasMinBedrooms(request.getBedrooms())
        );

        Specification<Property> spec = specs.stream()
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse(null);

        return propertyRepository.findAll(spec, pageable)
                .map(this::mapToPropertyResponse);
    }

    private PropertySearchResponse mapToPropertyResponse(Property property) {
        return new PropertySearchResponse(
                property.getId(),
                property.getRentcastId(),
                property.getFormattedAddress(),
                property.getAddressLine1(),
                property.getAddressLine2(),
                property.getCity(),
                property.getState(),
                property.getZipCode(),
                property.getCounty(),
                property.getLatitude(),
                property.getLongitude(),
                property.getPropertyType(),
                property.getBedrooms(),
                property.getBathrooms(),
                property.getSquareFootage(),
                property.getLotSize(),
                property.getYearBuilt(),
                property.getLastSaleDate(),
                property.getLastSalePrice(),
                property.getHoaFee(),
                property.getCreatedAt(),
                property.getUpdatedAt()
        );
    }
}
