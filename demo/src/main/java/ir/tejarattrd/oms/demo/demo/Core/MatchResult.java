package ir.tejarattrd.oms.demo.demo.Core;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Entity.Trade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * این کلاس نتایج عملیات تطبیق سفارش را نگهداری می‌کند.
 * شامل لیستی از معاملات انجام شده و مجموعه‌ای از سفارشات آپدیت شده است.
 */
public class MatchResult {

    private final List<Trade> trades = new ArrayList<>();
    private final Set<Order> updatedOrders = new HashSet<>(); // استفاده از Set برای جلوگیری از آپدیت تکراری

    public void addTrade(Trade trade) {
        this.trades.add(trade);
    }

    public void addUpdatedOrder(Order order) {
        this.updatedOrders.add(order);
    }

    /**
     * متد گمشده برای دسترسی به لیست معاملات اضافه شد.
     * @return لیستی از معاملات انجام شده در این عملیات.
     */
    public List<Trade> getTrades() {
        return this.trades;
    }

    public Collection<Order> getUpdatedOrders() {
        return this.updatedOrders;
    }
}