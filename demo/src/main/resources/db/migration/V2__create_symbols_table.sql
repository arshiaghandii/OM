CREATE TABLE symbols (
    symbol_id BIGSERIAL PRIMARY KEY,
    symbol_code VARCHAR(255) NOT NULL UNIQUE,
    company_name VARCHAR(255),
    market VARCHAR(255),
    unit_price NUMERIC(18, 2),
    -- این ستون باید اینجا باشد
    trading_volume BIGINT,
    last_price_update TIMESTAMP
);