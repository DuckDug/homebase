package com.homebase.homebase.controller;

import com.homebase.homebase.dto.StockQuoteResponse;
import com.homebase.homebase.service.StockQuoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock-quote")
public class StockQuoteController {

    private final StockQuoteService stockQuoteService;

    public StockQuoteController(StockQuoteService stockQuoteService) {
        this.stockQuoteService = stockQuoteService;
    }

    @GetMapping
    public ResponseEntity<Page<StockQuoteResponse>> searchLatestQuotes(@RequestParam(required = false) String symbol, Pageable pageable) {
        Page<StockQuoteResponse> stockQuoteResponsePage = stockQuoteService.findLatestQuotes(symbol, pageable);
        return ResponseEntity.ok().body(stockQuoteResponsePage);
    }
}
