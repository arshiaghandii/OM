package ir.tejarattrd.oms.demo.demo.Core;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class OrderBook {
    // سفارش‌های فروش (Asks) بر اساس کمترین قیمت مرتب شده‌اند
    private final NavigableMap<BigDecimal, List<Order>> asks = new TreeMap<>();
    // سفارش‌های خرید (Bids) بر اساس بیشترین قیمت مرتب شده‌اند
    private final NavigableMap<BigDecimal, List<Order>> bids = new TreeMap<>(Collections.reverseOrder());
    private final ReentrantLock lock = new ReentrantLock();

    public void addOrder(Order order) {
        lock.lock();
        try {
            NavigableMap<BigDecimal, List<Order>> sideBook = (order.getSide() == Order.OrderSide.BUY) ? bids : asks;
            sideBook.computeIfAbsent(order.getPrice(), k -> new LinkedList<>()).add(order);
        } finally { lock.unlock(); }
    }

    public void removeOrder(Order order) {
        lock.lock();
        try {
            NavigableMap<BigDecimal, List<Order>> sideBook = (order.getSide() == Order.OrderSide.BUY) ? bids : asks;
            List<Order> ordersAtPrice = sideBook.get(order.getPrice());
            if (ordersAtPrice != null) {
                ordersAtPrice.remove(order);
                if (ordersAtPrice.isEmpty()) {
                    sideBook.remove(order.getPrice());
                }
            }
        } finally { lock.unlock(); }
    }

    public NavigableMap<BigDecimal, List<Order>> getBids() { return bids; }
    public NavigableMap<BigDecimal, List<Order>> getAsks() { return asks; }
    public void lock() { lock.lock(); }
    public void unlock() { lock.unlock(); }
}