// Tejarat Project/demo/src/main/java/ir/tejarattrd/oms/demo/demo/DTO/SymbolDto.java

package ir.tejarattrd.oms.demo.demo.DTO;

import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import java.time.LocalDateTime;

public class SymbolDto {

    private Long symbolId;
    private String symbolCode;
    private String companyName;
    private String market;
    private Double unitPrice;
    private Long tradingVolume;
    private LocalDateTime lastPriceUpdate;

    // سازنده برای تبدیل راحت Entity به DTO
    public SymbolDto(Symbol symbol) {
        this.symbolId = symbol.getSymbolId();
        this.symbolCode = symbol.getSymbolCode();
        this.companyName = symbol.getCompanyName();
        this.market = symbol.getMarket();
        this.unitPrice = symbol.getUnitPrice();
        this.tradingVolume = symbol.getTradingVolume();
        this.lastPriceUpdate = symbol.getLastPriceUpdate();
    }

    // Getters and Setters...
    public Long getSymbolId() { return symbolId; }
    public void setSymbolId(Long symbolId) { this.symbolId = symbolId; }
    public String getSymbolCode() { return symbolCode; }
    public void setSymbolCode(String symbolCode) { this.symbolCode = symbolCode; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getMarket() { return market; }
    public void setMarket(String market) { this.market = market; }
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
    public Long getTradingVolume() { return tradingVolume; }
    public void setTradingVolume(Long tradingVolume) { this.tradingVolume = tradingVolume; }
    public LocalDateTime getLastPriceUpdate() { return lastPriceUpdate; }
    public void setLastPriceUpdate(LocalDateTime lastPriceUpdate) { this.lastPriceUpdate = lastPriceUpdate; }
}