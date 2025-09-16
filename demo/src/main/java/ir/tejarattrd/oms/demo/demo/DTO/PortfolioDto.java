package ir.tejarattrd.oms.demo.demo.DTO;

import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;
import java.math.BigDecimal;

public record PortfolioDto(
        String symbolCode,
        String companyName,
        BigDecimal quantity,
        BigDecimal averageBuyPrice,
        BigDecimal currentPrice,
        BigDecimal currentValue,
        BigDecimal profitOrLoss,
) {
    public PortfolioDto(PortfolioItem item) {
        this(
                String.valueOf(item.getSymbol().getSymbolCode()),
                item.getSymbol().getCompanyName(),
                new BigDecimal(item.getQuantity()),
                item.getAverageBuyPrice(), // مقدار برای averageBuyPrice
                item.getSymbol().getUnitPrice(),
                new BigDecimal(item.getQuantity()).multiply(item.getSymbol().getUnitPrice()),
                BigDecimal.ZERO, // فعلا سود و زیان را صفر در نظر می‌گیریم
                item.getAveragePrice() // <-- FIX: اضافه کردن مقدار برای فیلد averagePrice
        );
    }
}