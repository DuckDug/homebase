package com.homebase.homebase.controller;

import com.homebase.homebase.dto.PropertySearchRequest;
import com.homebase.homebase.dto.PropertySearchResponse;
import com.homebase.homebase.service.PropertyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/property")
public class PropertyController {
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public ResponseEntity<Page<PropertySearchResponse>> searchProperties(
            @ModelAttribute PropertySearchRequest propertySearchRequest,
            Pageable pageable
    ) {
        Page<PropertySearchResponse> properties = propertyService.findProperties(pageable, propertySearchRequest);

        return ResponseEntity.ok().body(properties);
    }
}
