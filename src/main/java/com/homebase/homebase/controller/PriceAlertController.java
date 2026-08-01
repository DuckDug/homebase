package com.homebase.homebase.controller;

import com.homebase.homebase.dto.PriceAlertCreateRequest;
import com.homebase.homebase.dto.PriceAlertResponse;
import com.homebase.homebase.dto.PriceAlertUpdateRequest;
import com.homebase.homebase.model.PriceAlertStatus;
import com.homebase.homebase.service.PriceAlertService;
import com.homebase.homebase.service.UserContextService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/price-alerts")
public class PriceAlertController {

    private final PriceAlertService priceAlertService;
    private final UserContextService userContextService;

    public PriceAlertController(PriceAlertService priceAlertService, UserContextService userContextService) {
        this.priceAlertService = priceAlertService;
        this.userContextService = userContextService;
    }

    @PostMapping
    public ResponseEntity<PriceAlertResponse> addPriceAlert(@Valid @RequestBody PriceAlertCreateRequest priceAlertCreateRequest, Authentication authentication) {
        Long userId = userContextService.getUserId(authentication);
        PriceAlertResponse priceAlertResponse = priceAlertService.createPriceAlert(userId, priceAlertCreateRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(priceAlertResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(priceAlertResponse);
    }

    @GetMapping
    public ResponseEntity<List<PriceAlertResponse>> getPriceAlerts(Authentication authentication, @RequestParam Optional<PriceAlertStatus> status) {
        Long userId = userContextService.getUserId(authentication);
        return status.map(priceAlertStatus -> ResponseEntity.ok().body(priceAlertService.getPriceAlerts(userId, priceAlertStatus))).orElseGet(() -> ResponseEntity.ok().body(priceAlertService.getPriceAlerts(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceAlertResponse> getPriceAlertById(Authentication authentication, @PathVariable Long id) {
        Long userId = userContextService.getUserId(authentication);
        return  ResponseEntity.ok().body(priceAlertService.getPriceAlertById(userId, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PriceAlertResponse> updatePriceAlert(Authentication authentication, @Valid @RequestBody PriceAlertUpdateRequest priceAlertUpdateRequest, @PathVariable Long id) {
        Long userId = userContextService.getUserId(authentication);
        return ResponseEntity.ok().body(priceAlertService.updatePriceAlert(userId, id, priceAlertUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriceAlert(Authentication authentication, @PathVariable Long id) {
        Long userId = userContextService.getUserId(authentication);
        priceAlertService.deletePriceAlert(userId, id);
        return ResponseEntity.noContent().build();
    }
}
