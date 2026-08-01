ALTER TABLE price_alerts ADD COLUMN notified_at TIMESTAMP;

CREATE INDEX idx_price_alerts_status_notified_at ON price_alerts (status, notified_at);