package com.homebase.homebase.job;

import com.homebase.homebase.client.StockApiClient;
import com.homebase.homebase.model.StockQuote;
import com.homebase.homebase.repository.StockQuoteRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class StockIngestionJob {
    private final StockQuoteRepository stockQuoteRepository;
    private final StockApiClient stockApiClient;

    public StockIngestionJob(StockQuoteRepository stockQuoteRepository, StockApiClient stockApiClient) {
        this.stockQuoteRepository = stockQuoteRepository;
        this.stockApiClient = stockApiClient;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void fetchStockQuote() {
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
                }
            }

            try {
                /* Only allowed 5 requests per minute */
                Thread.sleep(13000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


        }

    }
}
