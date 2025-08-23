
CREATE TABLE orders (
    -- شناسه منحصر به فرد هر سفارش
    order_id BIGSERIAL PRIMARY KEY,

    -- شناسه مشتری که این سفارش را ثبت کرده است
    customer_id BIGINT NOT NULL,

    -- شناسه نمادی که برای آن سفارش ثبت شده است
    symbol_id BIGINT NOT NULL,

    -- نوع سفارش (مثلاً "buy" یا "sell")
    order_type VARCHAR(50),

    -- تعداد سهام درخواستی
    quantity NUMERIC(18, 2),

    -- قیمت کل سفارش
    total_price NUMERIC(18, 2),

    -- تاریخ و زمان ثبت سفارش
    order_date TIMESTAMP,

    -- وضعیت سفارش (مثلاً "pending", "completed", "cancelled")
    status VARCHAR(50),

    -- تعریف کلید خارجی برای اتصال به جدول مشتریان
    -- اگر یک مشتری حذف شود، سفارش‌های او نیز حذف خواهد شد (ON DELETE CASCADE)
    CONSTRAINT fk_customer
        FOREIGN KEY(customer_id)
        REFERENCES customers(id)
        ON DELETE CASCADE,

    -- تعریف کلید خارجی برای اتصال به جدول نمادها
    CONSTRAINT fk_symbol
        FOREIGN KEY(symbol_id)
        REFERENCES symbols(symbol_id)
        ON DELETE CASCADE
);