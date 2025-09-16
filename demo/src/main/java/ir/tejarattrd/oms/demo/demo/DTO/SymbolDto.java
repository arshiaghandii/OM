package ir.tejarattrd.oms.demo.demo.DTO;

import ir.tejarattrd.oms.demo.demo.Entity.Symbol;

import java.math.BigDecimal;

public class SymbolDto {
    private Long id;
    // --- FIX: Changed 'name' to 'companyName' to reflect the entity ---
    private String companyName;
    private String description;
    private BigDecimal unitPrice;
    private Long tradingVolume;

    public SymbolDto(Symbol symbol) {
        this.id = symbol.getSymbol_id();
        this.companyName = symbol.getCompanyName();
        this.description = symbol.getDescription();
        this.unitPrice = symbol.getUnitPrice();
        this.tradingVolume = symbol.getTradingVolume();
    }

    public SymbolDto() {
    }

    public static SymbolDto fromEntity(Symbol symbol) {
        SymbolDto dto = new SymbolDto();
        dto.setId(symbol.getSymbol_id());
        // --- FIX: Also changed it here ---
        dto.setCompanyName(symbol.getCompanyName());
        dto.setDescription(symbol.getDescription());
        return dto;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // --- FIX: Renamed getter and setter for 'name' to 'companyName' ---
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Long getTradingVolume() { return tradingVolume; }
    public void setTradingVolume(Long tradingVolume) { this.tradingVolume = tradingVolume; }
}