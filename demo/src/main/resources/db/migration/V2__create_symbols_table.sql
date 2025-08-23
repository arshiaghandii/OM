
CREATE TABLE symbols (
    -- شناسه منحصر به فرد هر نماد که به صورت خودکار افزایش می‌یابد
    symbol_id BIGSERIAL PRIMARY KEY,

    -- کد منحصر به فرد نماد (مثلاً "خودرو") که نباید تکراری باشد
    symbol_code VARCHAR(255) NOT NULL UNIQUE,

    -- نام کامل شرکت مربوط به نماد
    company_name VARCHAR(255),

    -- نام بازار (مثلاً "بورس تهران")
    market VARCHAR(255),

    -- قیمت واحد هر سهم
    unit_price NUMERIC(18, 2), -- استفاده از NUMERIC برای دقت مالی بهتر است

    -- تاریخ و زمان آخرین به‌روزرسانی قیمت
    last_price_update TIMESTAMP
);