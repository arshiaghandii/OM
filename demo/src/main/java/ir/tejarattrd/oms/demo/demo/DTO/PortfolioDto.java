package ir.tejarattrd.oms.demo.demo.DTO;

import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;
import java.math.BigDecimal;

public record PortfolioDto(
        String symbolCode,
        String companyName,
        BigDecimal quantity,      // تعداد سهمی که کاربر دارد
        BigDecimal averageBuyPrice, // (برای آینده) میانگین قیمت خرید
        BigDecimal currentPrice,    // قیمت لحظه‌ای سهم
        BigDecimal currentValue,    // ارزش روز دارایی (تعداد * قیمت لحظه‌ای)
        BigDecimal profitOrLoss     // (برای آینده) میزان سود یا زیان
) {
    // یک سازنده کمکی برای ساخت DTO از انتیتی
    public PortfolioDto(PortfolioItem item) {
        this(
                item.getSymbol().getSymbolCode(),
                item.getSymbol().getCompanyName(),
                item.getQuantity(),
                BigDecimal.ZERO, // فعلا میانگین خرید را صفر در نظر می‌گیریم
                item.getSymbol().getUnitPrice(),
                item.getQuantity().multiply(item.getSymbol().getUnitPrice()), // محاسبه ارزش روز
                BigDecimal.ZERO // فعلا سود و زیان را صفر در نظر می‌گیریم
        );
    }
}
