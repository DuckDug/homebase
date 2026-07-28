CREATE TABLE price_alerts
(
    id           BIGSERIAL PRIMARY KEY,                         -- auto-incrementing primary key
    user_id      BIGINT         NOT NULL REFERENCES users (id), -- user id
    symbol       VARCHAR(10)    NOT NULL,                       -- stock symbol
    target_price DECIMAL(10, 2) NOT NULL,                       -- target price set by user
    condition    VARCHAR(10)    NOT NULL,                       -- condition either ABOVE/BELOW
    status       VARCHAR(10)    NOT NULL DEFAULT 'ACTIVE',      -- status ACTIVE/TRIGGERED
    created_at   TIMESTAMP      NOT NULL DEFAULT NOW(),         -- time price alert was created
    updated_at   TIMESTAMP      NOT NULL DEFAULT NOW()          -- time price alert was last updated
);

COMMENT ON TABLE price_alerts IS 'Users price alert table';
COMMENT ON COLUMN price_alerts.symbol IS 'Stores Stock Symbol';
COMMENT ON COLUMN price_alerts.status IS 'Stores Price Alert Status: Active/Triggered';

CREATE INDEX idx_price_alerts_user_id ON price_alerts (user_id);
CREATE INDEX idx_price_alerts_symbol ON price_alerts (symbol);
CREATE INDEX idx_price_alerts_symbol_status ON price_alerts (symbol, status);