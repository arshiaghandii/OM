package ir.tejarattrd.oms.demo.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime; // <-- 1. Import LocalDateTime

@Entity
public class Symbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tradingVolume;
    private String name;
    private String companyName;
    private String Market;
    private double unitPrice;

    // --- FIX HERE ---
    private LocalDateTime lastPriceUpdate; // <-- 2. Change the type to LocalDateTime
    // ----------------

    private String description;
    private int code;

    // Getters and Setters for other fields...

    public Long getTradingVolume() {
        return tradingVolume;
    }

    public void setTradingVolume(Long tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    public String getMarket() {
        return Market;
    }

    public void setMarket(String market) {
        Market = market;
    }

    // --- FIX HERE ---
    public LocalDateTime getLastPriceUpdate() { // <-- 3. Change the return type
        return lastPriceUpdate;
    }

    public void setLastPriceUpdate(LocalDateTime lastPriceUpdate) { // <-- 4. Change the parameter type
        this.lastPriceUpdate = lastPriceUpdate;
    }
    // ----------------

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getSymbolCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}