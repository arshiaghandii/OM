package ir.tejarattrd.oms.demo.demo.DTO;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

public class OrderDto {

        private Long orderId;
        private String symbolCode; // به جای کل آبجکت Symbol، فقط کد آن را می‌فرستیم
        private String orderType;
        private Double quantity;
        private Double totalPrice;
        private LocalDateTime orderDate;
        private String status;

        // سازنده‌ای برای تبدیل راحت Order Entity به OrderDto
        public OrderDto(Order order) {
        this.orderId = order.getOrderId();
        this.symbolCode = order.getSymbol().getSymbolCode(); // Flattening the data
        this.orderType = order.getOrderType();
        this.quantity = order.getQuantity();
        this.totalPrice = order.getTotalPrice();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
    }

        // Getters and Setters for all fields...
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public String getSymbolCode() { return symbolCode; }
        public void setSymbolCode(String symbolCode) { this.symbolCode = symbolCode; }
        public String getOrderType() { return orderType; }
        public void setOrderType(String orderType) { this.orderType = orderType; }
        public Double getQuantity() { return quantity; }
        public void setQuantity(Double quantity) { this.quantity = quantity; }
        public Double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
        public LocalDateTime getOrderDate() { return orderDate; }
        public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

/*-------------------------------------------------------------------------------------------------*/



    }
