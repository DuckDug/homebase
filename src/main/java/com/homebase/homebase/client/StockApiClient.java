package com.homebase.homebase.client;

import com.homebase.homebase.model.StockQuote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class StockApiClient {
    private final WebClient webClient;

    @Value("${alpha-vantage.api-key}")
    private String apiKey;

    public StockApiClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://www.alphavantage.co/").build();
    }

    @SuppressWarnings("unchecked")
    public Optional<StockQuote> fetchStockQuotes(String symbol) {
        Map<String, Object> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", "GLOBAL_QUOTE")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null) return Optional.empty();

        Map<String, Object> globalQuote = (Map<String, Object>) response.get("Global Quote");
        if (globalQuote == null) return Optional.empty();

        return Optional.of(mapToStockQuote(globalQuote));
    }

    private StockQuote mapToStockQuote(Map<String, Object> map) {
        StockQuote.StockQuoteBuilder builder = StockQuote.builder()
                .symbol((String) map.get("01. symbol"))
                .open(new BigDecimal((String) map.get("02. open")))
                .high(new BigDecimal((String) map.get("03. high")))
                .low(new BigDecimal((String) map.get("04. low")))
                .price(new BigDecimal((String) map.get("05. price")))
                .volume(Long.parseLong((String) map.get("06. volume")))
                .quoteDate(LocalDate.parse((String) map.get("07. latest trading day")))
                .previousClose(new BigDecimal((String) map.get("08. previous close")));

        return builder.build();
    }

}
