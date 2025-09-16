-- Tejarat Project/demo/src/main/resources/db/migration/V3__create_orders_table.sql

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    symbol_id BIGINT NOT NULL,
    order_type VARCHAR(50),
    quantity NUMERIC(18, 2),
    remaining_quantity NUMERIC(18, 2),
    total_price NUMERIC(18, 2),
    order_date TIMESTAMP,
    status VARCHAR(50),
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_customer
        FOREIGN KEY(customer_id)
        REFERENCES customers(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_symbol
        FOREIGN KEY(symbol_id)
        REFERENCES symbols(symbol_id)
        ON DELETE CASCADE
);