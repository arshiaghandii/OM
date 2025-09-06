package ir.tejarattrd.oms.demo.demo.DTO;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import java.math.BigDecimal; // **ایمپورت جدید**
import java.time.LocalDateTime;

public record OrderDto(
        Long orderId,
        String symbolCode,
        String orderType,
        BigDecimal quantity, // **تغییر نوع**
        BigDecimal totalPrice, // **تغییر نوع**
        LocalDateTime orderDate,
        String status
) {
    public OrderDto(Order order) {
        this(
                order.getOrderId(),
                order.getSymbol().getSymbolCode(),
                order.getOrderType(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderDate(),
                order.getStatus()
        );
    }
}