package com.homebase.homebase.repository;

import com.homebase.homebase.model.StockQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockQuoteRepository extends JpaRepository<StockQuote, Long> {

    List<StockQuote> findBySymbol(String symbol);

    List<StockQuote> findByQuoteDate(LocalDate quoteDate);

    @Query(value = "SELECT DISTINCT ON (symbol) * FROM stock_quotes WHERE symbol IN :symbols ORDER BY symbol, quote_date DESC", nativeQuery = true)
    List<StockQuote> findLatestQuotesForSymbols(@Param("symbols") List<String> symbols);

    Optional<StockQuote> findBySymbolAndQuoteDate(String symbol, LocalDate quoteDate);

    Boolean existsBySymbolAndQuoteDate(String symbol, LocalDate quoteDate);
}
