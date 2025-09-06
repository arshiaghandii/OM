package ir.tejarattrd.oms.demo.demo.DTO;

import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import java.math.BigDecimal; // **ایمپورت جدید**
import java.time.LocalDateTime;

public record SymbolDto(
        Long symbolId,
        String symbolCode,
        String companyName,
        String market,
        BigDecimal unitPrice, // **تغییر نوع**
        Long tradingVolume,
        LocalDateTime lastPriceUpdate
) {
    public SymbolDto(Symbol symbol) {
        this(
                symbol.getSymbolId(),
                symbol.getSymbolCode(),
                symbol.getCompanyName(),
                symbol.getMarket(),
                symbol.getUnitPrice(),
                symbol.getTradingVolume(),
                symbol.getLastPriceUpdate()
        );
    }
}