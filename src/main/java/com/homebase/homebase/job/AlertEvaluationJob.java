package com.homebase.homebase.job;

import com.homebase.homebase.model.*;
import com.homebase.homebase.repository.JobLogRepository;
import com.homebase.homebase.repository.PriceAlertRepository;
import com.homebase.homebase.repository.StockQuoteRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AlertEvaluationJob {

    private final PriceAlertRepository priceAlertRepository;
    private final StockQuoteRepository stockQuoteRepository;
    private final JobLogRepository jobLogRepository;
    private static final String JOBNAME = "AlertEvaluationJob";

    public AlertEvaluationJob(
            PriceAlertRepository priceAlertRepository,
            StockQuoteRepository stockQuoteRepository,
            JobLogRepository jobLogRepository
    ) {
        this.priceAlertRepository = priceAlertRepository;
        this.stockQuoteRepository = stockQuoteRepository;
        this.jobLogRepository = jobLogRepository;
    }

    public void evaluatePriceAlerts() {
        LocalDateTime start = LocalDateTime.now();
        String status = "SUCCESS";
        String errorMessage = null;
        int recordsProcessed = 0;

        try {
            List<PriceAlert> activeAlerts = priceAlertRepository.findByStatus(PriceAlertStatus.ACTIVE);

            if (activeAlerts.isEmpty()) {
                LocalDateTime end = LocalDateTime.now();

                JobLog.JobLogBuilder builder = JobLog.builder()
                        .jobName(JOBNAME)
                        .startedAt(start)
                        .finishedAt(end)
                        .status(status)
                        .errorMessage(errorMessage)
                        .recordsProcessed(recordsProcessed);

                jobLogRepository.save(builder.build());
                return;
            }
            List<String> symbols = activeAlerts.stream()
                    .map(PriceAlert::getSymbol)
                    .distinct()
                    .toList();

            List<StockQuote> latestQuotes = stockQuoteRepository.findLatestQuotesForSymbols(symbols);

            Map<String, StockQuote> latestQuotesBySymbol = latestQuotes.stream()
                    .collect(Collectors.toMap(StockQuote::getSymbol, q -> q));

            for (PriceAlert priceAlert : activeAlerts) {
                StockQuote latestQuoteBySymbol = latestQuotesBySymbol.get(priceAlert.getSymbol());

                if (latestQuoteBySymbol == null) {
                    continue;
                }
                recordsProcessed++;
                if (priceAlert.getCondition() == PriceAlertCondition.ABOVE &&
                        latestQuoteBySymbol.getPrice().compareTo(priceAlert.getTargetPrice()) >= 0
                ) {
                    priceAlert.setStatus(PriceAlertStatus.TRIGGERED);
                    priceAlert.setUpdatedAt(LocalDateTime.now());
                }
                else if (priceAlert.getCondition() == PriceAlertCondition.BELOW &&
                        latestQuoteBySymbol.getPrice().compareTo(priceAlert.getTargetPrice()) <= 0
                ) {
                    priceAlert.setStatus(PriceAlertStatus.TRIGGERED);
                    priceAlert.setUpdatedAt(LocalDateTime.now());
                }
            }

            priceAlertRepository.saveAll(activeAlerts);

        } catch (Exception e) {
            status = "FAILED";
            errorMessage = e.getMessage();
        } finally {
            LocalDateTime end = LocalDateTime.now();

            JobLog.JobLogBuilder builder = JobLog.builder()
                    .jobName(JOBNAME)
                    .startedAt(start)
                    .finishedAt(end)
                    .status(status)
                    .errorMessage(errorMessage)
                    .recordsProcessed(recordsProcessed);
            jobLogRepository.save(builder.build());
        }
    }
}
