package com.homebase.homebase.job;

import com.homebase.homebase.client.StockApiClient;
import com.homebase.homebase.model.JobLog;
import com.homebase.homebase.model.StockQuote;
import com.homebase.homebase.repository.JobLogRepository;
import com.homebase.homebase.repository.StockQuoteRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class StockIngestionJob {
    private final StockQuoteRepository stockQuoteRepository;
    private final StockApiClient stockApiClient;
    private final JobLogRepository jobLogRepository;
    private static final String JOBNAME = "StockIngestionJob";

    public StockIngestionJob(
            StockQuoteRepository stockQuoteRepository,
            StockApiClient stockApiClient,
            JobLogRepository jobLogRepository
    ) {
        this.stockQuoteRepository = stockQuoteRepository;
        this.stockApiClient = stockApiClient;
        this.jobLogRepository = jobLogRepository;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void fetchStockQuote() {
        LocalDateTime start = LocalDateTime.now();
        String status = "SUCCESS";
        String errorMessage = null;
        int recordsProcessed = 0;

        try{
            List<String> symbols = List.of(
                    "VNQ",
                    "O",
                    "SPG",
                    "VICI",
                    "AMT"
            );
            for (String symbol : symbols) {
                Optional<StockQuote> stockQuotes = stockApiClient.fetchStockQuotes(symbol);
                if (stockQuotes.isPresent()) {
                    StockQuote stockQuote = stockQuotes.get();
                    if (!stockQuoteRepository.existsBySymbolAndQuoteDate(stockQuote.getSymbol(), stockQuote.getQuoteDate())) {
                        stockQuoteRepository.save(stockQuote);
                        recordsProcessed++;
                    }
                }

                try {
                    /* Only allowed 5 requests per minute */
                    Thread.sleep(13000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
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
