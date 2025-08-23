-- V1__create_customers_table.sql
-- ایجاد جدول مشتریان مطابق با Customer.java Entity

CREATE TABLE customers (
    -- شناسه منحصر به فرد هر مشتری که به صورت خودکار افزایش می‌یابد
    id BIGSERIAL PRIMARY KEY,

    -- نام مشتری
    first_name VARCHAR(255) NOT NULL,

    -- نام خانوادگی مشتری
    last_name VARCHAR(255) NOT NULL,

    -- نام کاربری منحصر به فرد
    username VARCHAR(255) UNIQUE,

    -- ایمیل منحصر به فرد
    email VARCHAR(255) UNIQUE,

    -- شماره تماس
    phone VARCHAR(255),

    -- رمز عبور (هش شده)
    password VARCHAR(255),

    -- تاریخ و زمان ایجاد حساب کاربری که به صورت خودکار ثبت می‌شود
    created_at TIMESTAMP
);