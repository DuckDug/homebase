CREATE TABLE stock_quotes (
    id              BIGSERIAL PRIMARY KEY,                      -- auto-incrementing primary key
    symbol          VARCHAR(10)  NOT NULL,                      -- ticker symbol e.g. VNQ, O, SPG
    name            VARCHAR(255),                               -- company/fund name
    price           DECIMAL(10, 2),                             -- current price
    open            DECIMAL(10, 2),                             -- opening price
    high            DECIMAL(10, 2),                             -- daily high
    low             DECIMAL(10, 2),                             -- daily low
    previous_close  DECIMAL(10, 2),                             -- previous day close price
    volume          BIGINT,                                     -- trading volume
    quote_date      DATE NOT NULL,                              -- date of the quote
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),           -- record creation timestamp
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),           -- last update timestamp
    UNIQUE (symbol, quote_date)                                 -- prevent duplicate quotes per day
);

COMMENT ON TABLE stock_quotes IS 'Stores daily stock and REIT price data ingested from Alpha Vantage';
COMMENT ON COLUMN stock_quotes.symbol IS 'Stock ticker symbol e.g. VNQ, O, SPG, VICI';
COMMENT ON COLUMN stock_quotes.quote_date IS 'The trading date this quote represents';

CREATE INDEX idx_stock_quotes_symbol ON stock_quotes (symbol);
CREATE INDEX idx_stock_quotes_quote_date ON stock_quotes (quote_date);
CREATE INDEX idx_stock_quotes_symbol_date ON stock_quotes (symbol, quote_date);