package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Order;

public interface TradingService {
    Order placeNewOrder(Order order);
    void cancelOrder(Long orderId);
    Order getOrderById(Long id);
}