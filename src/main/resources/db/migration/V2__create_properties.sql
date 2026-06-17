CREATE TABLE properties (
                            id                  BIGSERIAL PRIMARY KEY,                          -- auto-incrementing primary key
                            rentcast_id         VARCHAR(255) NOT NULL UNIQUE,                   -- unique RentCast property identifier
                            formatted_address   VARCHAR(500) NOT NULL,                          -- full address string
                            address_line1       VARCHAR(255),                                   -- street address
                            address_line2       VARCHAR(255),                                   -- unit/apt number
                            city                VARCHAR(100),                                   -- city
                            state               VARCHAR(2),                                     -- 2-char state abbreviation
                            zip_code            VARCHAR(10),                                    -- 5-digit zip code
                            county              VARCHAR(100),                                   -- county name
                            latitude            DECIMAL(10, 7),                                 -- geographic latitude
                            longitude           DECIMAL(10, 7),                                 -- geographic longitude
                            property_type       VARCHAR(100),                                   -- Single Family, Condo, etc.
                            bedrooms            INTEGER,                                        -- number of bedrooms
                            bathrooms           DECIMAL(4, 1),                                  -- number of bathrooms (allows 2.5)
                            square_footage      INTEGER,                                        -- interior living area in sq ft
                            lot_size            INTEGER,                                        -- lot size in sq ft
                            year_built          INTEGER,                                        -- year of construction
                            last_sale_date      TIMESTAMP,                                      -- date of last sale
                            last_sale_price     BIGINT,                                         -- price of last sale in dollars
                            hoa_fee             DECIMAL(10, 2),                                 -- monthly HOA fee
                            features            JSONB,                                          -- cooling, heating, garage, pool, etc.
                            tax_assessments     JSONB,                                          -- historical tax assessment data
                            property_taxes      JSONB,                                          -- historical property tax data
                            history             JSONB,                                          -- sale transaction history
                            created_at          TIMESTAMP NOT NULL DEFAULT NOW(),               -- record creation timestamp
                            updated_at          TIMESTAMP NOT NULL DEFAULT NOW()                -- last update timestamp
);

COMMENT ON TABLE properties IS 'Stores property records ingested from the Rentcast API';
COMMENT ON COLUMN properties.rentcast_id IS 'Unique identifier assigned by RentCast, used to deduplicate ingestion runs';
COMMENT ON COLUMN properties.features IS 'JSONB blob: architecture, cooling, heating, garage, pool, roof, view types';
COMMENT ON COLUMN properties.tax_assessments IS 'JSONB blob: yearly tax assessment history keyed by year';
COMMENT ON COLUMN properties.property_taxes IS 'JSONB blob: yearly property tax amounts keyed by year';
COMMENT ON COLUMN properties.history IS 'JSONB blob: sale transaction history keyed by sale date';

CREATE INDEX idx_properties_city_state ON properties (city, state);
CREATE INDEX idx_properties_zip_code ON properties (zip_code);
CREATE INDEX idx_properties_property_type ON properties (property_type);
CREATE INDEX idx_properties_bedrooms ON properties (bedrooms);
CREATE INDEX idx_properties_last_sale_price ON properties (last_sale_price);