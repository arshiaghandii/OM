-- Tejarat Project/demo/src/main/resources/db/migration/V2__create_symbols_table.sql

CREATE TABLE symbols (
    symbol_id BIGSERIAL PRIMARY KEY,
    symbol_code INT NOT NULL UNIQUE,
    company_name VARCHAR(255) NOT NULL,
    market VARCHAR(100),
    unit_price NUMERIC(19, 8),
    trading_volume BIGINT,
    last_price_update TIMESTAMP,
    description VARCHAR(255)
);