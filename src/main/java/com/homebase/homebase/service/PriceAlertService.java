package com.homebase.homebase.service;

import com.homebase.homebase.dto.PriceAlertCreateRequest;
import com.homebase.homebase.dto.PriceAlertResponse;
import com.homebase.homebase.exception.DuplicateResourceException;
import com.homebase.homebase.exception.ResourceNotFoundException;
import com.homebase.homebase.model.PriceAlert;
import com.homebase.homebase.model.PriceAlertCondition;
import com.homebase.homebase.model.PriceAlertStatus;
import com.homebase.homebase.repository.PriceAlertRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;

    public PriceAlertService(PriceAlertRepository priceAlertRepository) {
        this.priceAlertRepository = priceAlertRepository;
    }

    public PriceAlertResponse createPriceAlert(Long userId, PriceAlertCreateRequest priceAlertCreateRequest) {
        String symbol = priceAlertCreateRequest.getSymbol().toUpperCase();
        PriceAlertCondition condition = priceAlertCreateRequest.getCondition();
        BigDecimal targetPrice = priceAlertCreateRequest.getTargetPrice();

        if (priceAlertRepository.existsByUserIdAndSymbolAndConditionAndTargetPrice(userId, symbol, condition, targetPrice)) {
            throw new DuplicateResourceException(
                    "Price alert already exists: " +
                            "Symbol: " + symbol +
                            ", Condition: " + condition +
                            ", Target Price: " + targetPrice
            );
        }

        PriceAlert priceAlert = PriceAlert.builder()
                .userId(userId)
                .symbol(symbol)
                .targetPrice(targetPrice)
                .condition(condition)
                .status(PriceAlertStatus.ACTIVE)
                .build();

        PriceAlert saved = priceAlertRepository.save(priceAlert);

        return  mapToPriceAlertResponse(saved);
    }

    public PriceAlertResponse getPriceAlertById(Long userId, Long priceAlertId) {
        PriceAlert priceAlert = priceAlertRepository.findByIdAndUserId(priceAlertId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Price Alert", priceAlertId));

        return mapToPriceAlertResponse(priceAlert);
    }

    public List<PriceAlertResponse> getPriceAlerts(Long userId) {
        return priceAlertRepository.findByUserId(userId)
                .stream()
                .map(this::mapToPriceAlertResponse)
                .toList();

    }

    public List<PriceAlertResponse> getPriceAlerts(Long userId, PriceAlertStatus status) {
        return priceAlertRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(this::mapToPriceAlertResponse)
                .toList();
    }

    private PriceAlertResponse mapToPriceAlertResponse(PriceAlert priceAlert) {
        return new PriceAlertResponse(
                priceAlert.getId(),
                priceAlert.getSymbol(),
                priceAlert.getTargetPrice(),
                priceAlert.getCondition(),
                priceAlert.getStatus(),
                priceAlert.getCreatedAt(),
                priceAlert.getUpdatedAt()
        );
    }
}
