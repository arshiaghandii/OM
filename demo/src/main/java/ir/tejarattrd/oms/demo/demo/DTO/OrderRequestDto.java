package ir.tejarattrd.oms.demo.demo.DTO;

import ir.tejarattrd.oms.demo.demo.Entity.Order;

import java.math.BigDecimal;

/**
 * این کلاس برای دریافت اطلاعات یک سفارش جدید از طریق درخواست‌های API استفاده می‌شود.
 */

public class OrderRequestDto {

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(Long symbolId) {
        this.symbolId = symbolId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Order.OrderSide getSide() {
        return side;
    }

    public void setSide(Order.OrderSide side) {
        this.side = side;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private Long customerId;
    private Long symbolId;
    private Order.OrderSide side; // باید یکی از مقادیر BUY یا SELL باشد
    private long quantity;
    private BigDecimal price;

}