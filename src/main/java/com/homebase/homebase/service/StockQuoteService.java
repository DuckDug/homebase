package com.homebase.homebase.service;

import com.homebase.homebase.dto.StockQuoteResponse;
import com.homebase.homebase.model.StockQuote;
import com.homebase.homebase.repository.StockQuoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StockQuoteService {

    private final StockQuoteRepository stockQuoteRepository;

    public StockQuoteService(StockQuoteRepository stockQuoteRepository) {
        this.stockQuoteRepository = stockQuoteRepository;
    }

    public Page<StockQuoteResponse> findLatestQuotes(String symbol, Pageable pageable) {
        return stockQuoteRepository.findLatestQuotes(symbol, pageable)
                .map(this::mapToStockQuoteResponse);
    }

    private StockQuoteResponse mapToStockQuoteResponse(StockQuote stockQuote) {
        return new StockQuoteResponse(
                stockQuote.getId(),
                stockQuote.getSymbol(),
                stockQuote.getName(),
                stockQuote.getPrice(),
                stockQuote.getOpen(),
                stockQuote.getHigh(),
                stockQuote.getLow(),
                stockQuote.getPreviousClose(),
                stockQuote.getVolume(),
                stockQuote.getQuoteDate(),
                stockQuote.getCreatedAt(),
                stockQuote.getUpdatedAt()
        );
    }
}
