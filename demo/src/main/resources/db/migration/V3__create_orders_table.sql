CREATE TABLE orders (
    -- شناسه منحصر به فرد هر سفارش (تغییر از order_id به id)
    id BIGSERIAL PRIMARY KEY,

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
    CONSTRAINT fk_customer
        FOREIGN KEY(customer_id)
        REFERENCES customers(id)
        ON DELETE CASCADE,

    -- تعریف کلید خارجی برای اتصال به جدول نمادها (تغییر از symbol_id به id)
    CONSTRAINT fk_symbol
        FOREIGN KEY(symbol_id)
        REFERENCES symbols(id) -- <<-- این خط نیز اصلاح شد
        ON DELETE CASCADE
);