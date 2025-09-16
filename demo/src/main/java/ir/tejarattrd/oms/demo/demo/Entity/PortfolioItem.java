package ir.tejarattrd.oms.demo.demo.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal; // ایمپورت کردن کلاس مورد نیاز

@Entity
@Table(name = "portfolio_items")
public class PortfolioItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol_id", nullable = false)
    private Symbol symbol;

    @Column(nullable = false)
    private long quantity; // نوع داده به long تغییر کرد تا اعداد بزرگتر را پشتیبانی کند

    // --- FIX: این فیلد کلیدی اضافه شد ---
    @Column(name = "average_price", nullable = false, precision = 19, scale = 8)
    private BigDecimal averagePrice;


    // ------------------------- Getters and Setters -------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    // --- FIX: متدهای مربوط به فیلد جدید اضافه شد ---
    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }
}