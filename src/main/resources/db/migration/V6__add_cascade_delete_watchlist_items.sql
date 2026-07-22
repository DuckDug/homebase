ALTER TABLE watchlist_items DROP CONSTRAINT watchlist_items_watchlist_id_fkey;

ALTER TABLE watchlist_items
    ADD CONSTRAINT watchlist_items_watchlist_id_fkey
        FOREIGN KEY (watchlist_id) REFERENCES watchlists (id)
            ON DELETE CASCADE;