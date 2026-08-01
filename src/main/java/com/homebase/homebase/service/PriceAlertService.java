package com.homebase.homebase.service;

import com.homebase.homebase.dto.PriceAlertCreateRequest;
import com.homebase.homebase.dto.PriceAlertResponse;
import com.homebase.homebase.dto.PriceAlertUpdateRequest;
import com.homebase.homebase.exception.DuplicateResourceException;
import com.homebase.homebase.exception.NoChangesProvidedException;
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

        checkForDuplicate(userId, symbol, condition, targetPrice);

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

    public PriceAlertResponse updatePriceAlert(Long userId, Long priceAlertId, PriceAlertUpdateRequest priceAlertUpdateRequest) {

        BigDecimal targetPrice = priceAlertUpdateRequest.getTargetPrice();
        PriceAlertCondition condition = priceAlertUpdateRequest.getCondition();

        if (targetPrice == null && condition == null) {
            throw new NoChangesProvidedException("Price Alert", priceAlertId);
        }

        PriceAlert priceAlert = priceAlertRepository.findByIdAndUserId(priceAlertId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Price Alert", priceAlertId));

        BigDecimal effectiveTargetPrice = targetPrice != null ? targetPrice : priceAlert.getTargetPrice();
        PriceAlertCondition effectiveCondition = condition != null ? condition : priceAlert.getCondition();
        String symbol = priceAlert.getSymbol();

        checkForDuplicate(userId, symbol, effectiveCondition, effectiveTargetPrice);

        if (targetPrice != null) {
            priceAlert.setTargetPrice(targetPrice);
        }

        if (condition != null) {
            priceAlert.setCondition(condition);
        }
        priceAlert.setStatus(PriceAlertStatus.ACTIVE);

        priceAlertRepository.save(priceAlert);
        return mapToPriceAlertResponse(priceAlert);
    }

    public void deletePriceAlert(Long userId, Long priceAlertId) {
        PriceAlert priceAlert = priceAlertRepository.findByIdAndUserId(priceAlertId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Price Alert", priceAlertId));

        priceAlertRepository.delete(priceAlert);
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

    private void checkForDuplicate(Long userId, String symbol, PriceAlertCondition condition, BigDecimal targetPrice) {
        if (priceAlertRepository.existsByUserIdAndSymbolAndConditionAndTargetPrice(userId, symbol, condition, targetPrice)) {
            throw new DuplicateResourceException(
                    "Price alert already exists: " +
                            "Symbol: " + symbol +
                            ", Condition: " + condition +
                            ", Target Price: " + targetPrice
            );
        }
    }
}
