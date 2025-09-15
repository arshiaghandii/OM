package ir.tejarattrd.oms.demo.demo.Entity;

import jakarta.persistence.Column; // <-- 1. Import the Column annotation
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Symbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "symbol_id") // <-- 2. Map this field to the 'symbol_id' column
    private Long id;

    @Column(name = "symbol_code") // <-- 3. Map to 'symbol_code'
    private int symbolCode;

    @Column(name = "company_name") // <-- 4. Map to 'company_name'
    private String companyName;

    @Column(name = "market") // <-- 5. Map to 'market'
    private String market;

    @Column(name = "unit_price") // <-- 6. Map to 'unit_price'
    private double unitPrice;

    @Column(name = "trading_volume") // <-- 7. Map to 'trading_volume'
    private Long tradingVolume;

    @Column(name = "last_price_update") // <-- 8. Map to 'last_price_update'
    private LocalDateTime lastPriceUpdate;

    // This field was in your original class but not in the database schema you provided.
    // If you don't need it, you can remove it. If you do, you'll need to add a 'description' column to your database.
    private String description;

    // --- Getters and Setters for the corrected fields ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSymbolCode() {
        return symbolCode;
    }

    public void setSymbolCode(int symbolCode) {
        this.symbolCode = symbolCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getTradingVolume() {
        return tradingVolume;
    }

    public void setTradingVolume(Long tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    public LocalDateTime getLastPriceUpdate() {
        return lastPriceUpdate;
    }

    public void setLastPriceUpdate(LocalDateTime lastPriceUpdate) {
        this.lastPriceUpdate = lastPriceUpdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}