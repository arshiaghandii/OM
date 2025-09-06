package ir.tejarattrd.oms.demo.demo.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal; // **ایمپورت جدید**
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "symbols")
public class Symbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long symbolId;

    @Column(name = "symbol_code", unique = true, nullable = false)
    private String symbolCode;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "market")
    private String market;

    // **تغییر کلیدی: نوع داده به BigDecimal تغییر کرد**
    @Column(name = "unit_price", precision = 18, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "trading_volume")
    private Long tradingVolume;

    @Column(name = "last_price_update")
    private LocalDateTime lastPriceUpdate;

    @OneToMany(mappedBy = "symbol", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    // Getters and Setters (با نوع BigDecimal)
    public Long getSymbolId() { return symbolId; }
    public void setSymbolId(Long symbolId) { this.symbolId = symbolId; }
    public String getSymbolCode() { return symbolCode; }
    public void setSymbolCode(String symbolCode) { this.symbolCode = symbolCode; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getMarket() { return market; }
    public void setMarket(String market) { this.market = market; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Long getTradingVolume() { return tradingVolume; }
    public void setTradingVolume(Long tradingVolume) { this.tradingVolume = tradingVolume; }
    public LocalDateTime getLastPriceUpdate() { return lastPriceUpdate; }
    public void setLastPriceUpdate(LocalDateTime lastPriceUpdate) { this.lastPriceUpdate = lastPriceUpdate; }
    public Set<Order> getOrders() { return orders; }
    public void setOrders(Set<Order> orders) { this.orders = orders; }
}