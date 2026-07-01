CREATE TABLE watchlists
(
    id         BIGSERIAL PRIMARY KEY,                       -- auto-incrementing primary key
    user_id    BIGINT       NOT NULL REFERENCES users (id), -- user id that can be joined with users table
    name       VARCHAR(100) NOT NULL,                       -- name of the watchlist
    status     VARCHAR(20),                                 -- status of watchlist (ACTIVE, INACTIVE)
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),         -- time watchlist was created
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW()          -- time watchlist was updated last
);

COMMENT ON TABLE watchlists IS 'Stores User Watchlist Template';
COMMENT ON COLUMN watchlists.name IS 'Stores name of the current watchlist';
COMMENT ON COLUMN watchlists.status IS 'Stores Watchlist Status as such : ACTIVE/INACTIVE';

CREATE INDEX idx_watchlists_name ON watchlists (name);
CREATE INDEX idx_watchlists_status ON watchlists (status);
CREATE INDEX idx_watchlists_user ON watchlists (user_id);
CREATE INDEX idx_watchlists_name_user ON watchlists (name, user_id);

CREATE TABLE watchlist_items
(
    id           BIGSERIAL PRIMARY KEY,                           -- auto-incrementing primary key
    watchlist_id BIGINT      NOT NULL REFERENCES watchlists (id), -- id to join with watchlists table
    item_type    VARCHAR(20) NOT NULL,                            -- either a Property Number or Stock Symbol
    reference_id VARCHAR(50) NOT NULL,                            -- id that can join on stock_quotes or properties table
    created_at   TIMESTAMP   NOT NULL DEFAULT NOW()               -- time watchlist_item was created
);

COMMENT ON TABLE watchlist_items IS 'Stores Watchlist Information';
COMMENT ON COLUMN watchlist_items.item_type IS 'Property Number or Stock Symbol';
COMMENT ON COLUMN watchlist_items.reference_id IS 'ID for either property or stock table row';

CREATE INDEX idx_watchlist_items_watchlist_id ON watchlist_items (watchlist_id);
CREATE INDEX idx_watchlist_items_item_type ON watchlist_items (item_type);
CREATE INDEX idx_watchlist_items_item_reference ON watchlist_items (item_type, reference_id);