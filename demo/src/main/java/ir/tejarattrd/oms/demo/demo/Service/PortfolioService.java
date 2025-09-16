package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.DTO.PortfolioDto;
import ir.tejarattrd.oms.demo.demo.Entity.Trade;

/**
 * این اینترفیس، عملیات مربوط به مدیریت پورتفولیوی مشتریان را تعریف می‌کند.
 */
public interface PortfolioService {

    /**
     * اطلاعات پورتفولیوی یک مشتری خاص را بر اساس نام کاربری او برمی‌گرداند.
     * @param username نام کاربری مشتری
     * @return یک DTO شامل موجودی و لیست دارایی‌های مشتری
     */
    PortfolioDto getPortfolioForCustomer(String username);

    /**
     * پس از انجام یک معامله، پورتفولیوی خریدار و فروشنده را به‌روزرسانی می‌کند.
     * @param trade معامله انجام شده
     */
    void updatePortfolioAfterTrade(Trade trade);
}
