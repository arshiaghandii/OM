// Tejarat Project/demo/src/main/java/ir/tejarattrd/oms/demo/demo/Entity/Symbol.java

package ir.tejarattrd.oms.demo.demo.Entity;

import jakarta.persistence.*; // <-- Table را از اینجا import کنید
import java.time.LocalDateTime;

// --- FIX: نام جدول به صراحت مشخص شد ---
@Entity
@Table(name = "symbols")
// --- FIX END ---
public class Symbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "symbol_id")
    private Long id;

    @Column(name = "symbol_code")
    private int symbolCode;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "market")
    private String market;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "trading_volume")
    private Long tradingVolume;

    @Column(name = "last_price_update")
    private LocalDateTime lastPriceUpdate;

    private String description;

    // --- Getters and Setters (بدون تغییر) ---
    // ... (بقیه کدها)
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