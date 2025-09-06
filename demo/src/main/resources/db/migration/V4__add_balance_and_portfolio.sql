

-- اضافه کردن ستون موجودی به جدول مشتریان با مقدار پیش‌فرض بالا برای تست
ALTER TABLE customers ADD COLUMN balance NUMERIC(19, 2) DEFAULT 1000000000.00;

-- ایجاد جدول برای نگهداری سبد سهام هر مشتری
CREATE TABLE portfolio_items (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    symbol_id BIGINT NOT NULL,
    quantity NUMERIC(18, 2) NOT NULL,

    -- تعریف کلیدهای خارجی
    CONSTRAINT fk_customer
        FOREIGN KEY(customer_id)
        REFERENCES customers(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_symbol
        FOREIGN KEY(symbol_id)
        REFERENCES symbols(symbol_id)
        ON DELETE CASCADE,

    -- هر کاربر برای هر نماد فقط یک رکورد در پورتفولیو دارد
    CONSTRAINT uk_customer_symbol UNIQUE (customer_id, symbol_id)
);