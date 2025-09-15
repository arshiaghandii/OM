package ir.tejarattrd.oms.demo.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol_id", nullable = false)
    private Symbol symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSide side; // BUY or SELL

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private long remainingQuantity;

    @Column(precision = 19, scale = 8, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private LocalDateTime createdAt;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.remainingQuantity = this.quantity;
        if (this.status == null) {
            this.status = OrderStatus.NEW;
        }
    }

    public enum OrderSide { BUY, SELL }
    public enum OrderStatus { NEW, OPEN, PARTIALLY_FILLED, FILLED, CANCELED }
}