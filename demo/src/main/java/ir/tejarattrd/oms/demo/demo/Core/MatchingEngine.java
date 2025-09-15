package ir.tejarattrd.oms.demo.demo.Core;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Entity.Trade;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.regex.MatchResult;

public class MatchingEngine {

    private final OrderBook orderBook;

    public MatchingEngine(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    /**
     * سفارش جدید را پردازش کرده، با سفارش‌های موجود تطبیق می‌دهد و نتیجه را برمی‌گرداند.
     * @param newOrder سفارش جدید برای پردازش
     * @return یک شی MatchResult شامل معاملات انجام شده و سفارشات آپدیت شده
     */
    public MatchResult processOrder(Order newOrder) {
        orderBook.lock();
        try {
            // یک شی جدید از MatchResult برای نگهداری نتایج این عملیات می‌سازیم
            MatchResult result = new MatchResult();

            if (newOrder.getSide() == Order.OrderSide.BUY) {
                match(newOrder, orderBook.getAsks(), result);
            } else {
                match(newOrder, orderBook.getBids(), result);
            }

            // اگر بخشی از سفارش باقی مانده بود، آن را به دفتر سفارشات اضافه می‌کنیم
            if (newOrder.getRemainingQuantity() > 0) {
                // وضعیت سفارش را بر اساس اینکه آیا معامله‌ای داشته یا نه، تعیین می‌کنیم
                newOrder.setStatus(newOrder.getQuantity() == newOrder.getRemainingQuantity() ? Order.OrderStatus.OPEN : Order.OrderStatus.PARTIALLY_FILLED);
                orderBook.addOrder(newOrder);
                result.addUpdatedOrder(newOrder);
            }
            return result;
        } finally {
            orderBook.unlock();
        }
    }

    /**
     * منطق اصلی تطبیق یک سفارش جدید با سفارش‌های طرف مقابل در دفتر سفارشات.
     * @param newOrder سفارش جدید
     * @param oppositeBook دفتر سفارشات طرف مقابل (مثلاً برای سفارش خرید، دفتر فروش)
     * @param result شی MatchResult برای ذخیره نتایج
     */
    private void match(Order newOrder, NavigableMap<BigDecimal, List<Order>> oppositeBook, MatchResult result) {
        Iterator<Map.Entry<BigDecimal, List<Order>>> iterator = oppositeBook.entrySet().iterator();

        while (iterator.hasNext() && newOrder.getRemainingQuantity() > 0) {
            Map.Entry<BigDecimal, List<Order>> entry = iterator.next();
            BigDecimal priceInBook = entry.getKey();
            List<Order> ordersAtPrice = entry.getValue();

            // شرط قیمت را برای تطبیق بررسی می‌کنیم
            boolean isPriceMatch = (newOrder.getSide() == Order.OrderSide.BUY && newOrder.getPrice().compareTo(priceInBook) >= 0) ||
                    (newOrder.getSide() == Order.OrderSide.SELL && newOrder.getPrice().compareTo(priceInBook) <= 0);

            if (!isPriceMatch) {
                break; // اگر قیمت نخورد، از حلقه خارج می‌شویم
            }

            Iterator<Order> orderIterator = ordersAtPrice.iterator();
            while (orderIterator.hasNext() && newOrder.getRemainingQuantity() > 0) {
                Order orderInBook = orderIterator.next();
                long tradeQuantity = Math.min(newOrder.getRemainingQuantity(), orderInBook.getRemainingQuantity());

                // یک معامله جدید ایجاد می‌کنیم
                Trade trade = (newOrder.getSide() == Order.OrderSide.BUY) ?
                        new Trade(newOrder.getSymbol().getName(), priceInBook, tradeQuantity, newOrder.getId(), orderInBook.getId()) :
                        new Trade(newOrder.getSymbol().getName(), priceInBook, tradeQuantity, orderInBook.getId(), newOrder.getId());
                result.addTrade(trade);

                // حجم باقی‌مانده هر دو سفارش را آپدیت می‌کنیم
                newOrder.setRemainingQuantity(newOrder.getRemainingQuantity() - tradeQuantity);
                orderInBook.setRemainingQuantity(orderInBook.getRemainingQuantity() - tradeQuantity);

                // وضعیت هر دو سفارش را آپدیت می‌کنیم
                updateOrderStatus(newOrder);
                updateOrderStatus(orderInBook);
                result.addUpdatedOrder(newOrder);
                result.addUpdatedOrder(orderInBook);

                // اگر سفارش موجود در دفتر کاملاً پر شد، آن را حذف می‌کنیم
                if (orderInBook.getRemainingQuantity() == 0) {
                    orderIterator.remove();
                }
            }

            // اگر در یک سطح قیمتی هیچ سفارشی باقی نماند، آن سطح را از دفتر حذف می‌کنیم
            if (ordersAtPrice.isEmpty()) {
                iterator.remove();
            }
        }
    }

    /**
     * وضعیت یک سفارش را بر اساس حجم باقی‌مانده آن آپدیت می‌کند.
     * @param order سفارشی که باید آپدیت شود
     */
    private void updateOrderStatus(Order order) {
        if (order.getRemainingQuantity() == 0) {
            order.setStatus(Order.OrderStatus.FILLED);
        } else if (order.getRemainingQuantity() < order.getQuantity()) {
            order.setStatus(Order.OrderStatus.PARTIALLY_FILLED);
        }
    }
}