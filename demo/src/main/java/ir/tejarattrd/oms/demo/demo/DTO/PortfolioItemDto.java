package ir.tejarattrd.oms.demo.demo.DTO;

import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;

public class PortfolioItemDto {
    private String symbolCode;
    private String companyName;
    private int quantity;
    private double currentValue;    // ارزش روز
    private double profitLoss;      // سود و زیان

    // --- Constructors ---
    public PortfolioItemDto() {
    }

    public PortfolioItemDto(String symbolCode, String companyName, int quantity, double currentValue, double profitLoss) {
        this.symbolCode = symbolCode;
        this.companyName = companyName;
        this.quantity = quantity;
        this.currentValue = currentValue;
        this.profitLoss = profitLoss;
    }

    public PortfolioItemDto(PortfolioItem portfolioItem) {
        
    }

    // --- Getters and Setters ---
    public String getSymbolCode() {
        return symbolCode;
    }

    public void setSymbolCode(String symbolCode) {
        this.symbolCode = symbolCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(double profitLoss) {
        this.profitLoss = profitLoss;
    }
}