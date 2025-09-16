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
    /**
     * یک سازنده کمکی برای ساخت DTO از انتیتی.
     * FIX: تمام تبدیل‌های نوع داده لازم برای هماهنگی با تعریف رکورد انجام شده است.
     * این کار خطای کامپایل 'int cannot be dereferenced' را برطرف می‌کند.
     */
    public PortfolioDto(PortfolioItem item) {
        this(
                // تبدیل int به String
                String.valueOf(item.getSymbol().getSymbolCode()),
                item.getSymbol().getCompanyName(),
                // تبدیل Long به BigDecimal
                new BigDecimal(item.getQuantity()),
                BigDecimal.ZERO, // فعلا میانگین خرید را صفر در نظر می‌گیریم
                // تبدیل double به BigDecimal
                BigDecimal.valueOf(item.getSymbol().getUnitPrice()),
                // انجام عملیات ضرب با استفاده از BigDecimal
                new BigDecimal(item.getQuantity()).multiply(BigDecimal.valueOf(item.getSymbol().getUnitPrice())),
                BigDecimal.ZERO // فعلا سود و زیان را صفر در نظر می‌گیریم
        );
    }
}
