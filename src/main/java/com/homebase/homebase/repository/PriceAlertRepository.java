package com.homebase.homebase.repository;

import com.homebase.homebase.model.PriceAlert;
import com.homebase.homebase.model.PriceAlertCondition;
import com.homebase.homebase.model.PriceAlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {

    List<PriceAlert> findByUserId(Long userId);

    List<PriceAlert> findByStatus(PriceAlertStatus status);

    List<PriceAlert> findBySymbolAndStatus(String symbol, PriceAlertStatus status);

    List<PriceAlert> findByUserIdAndStatus(Long userId, PriceAlertStatus status);

    List<PriceAlert> findByStatusAndNotifiedAtIsNull(PriceAlertStatus status);

    Optional<PriceAlert> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserIdAndSymbolAndConditionAndTargetPrice(Long userId, String symbol, PriceAlertCondition condition, BigDecimal targetPrice);

}
