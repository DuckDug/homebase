package com.homebase.homebase.repository;

import com.homebase.homebase.model.StockQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockQuoteRepository extends JpaRepository<StockQuote, Long> {

    List<StockQuote> findBySymbol(String symbol);

    List<StockQuote> findByQuoteDate(LocalDate quoteDate);

    Optional<StockQuote> findBySymbolAndQuoteDate(String symbol, LocalDate quoteDate);

    Boolean existsBySymbolAndQuoteDate(String symbol, LocalDate quoteDate);
}
