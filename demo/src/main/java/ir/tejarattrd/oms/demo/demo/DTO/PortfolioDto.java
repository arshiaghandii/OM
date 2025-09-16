package ir.tejarattrd.oms.demo.demo.DTO;

import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record PortfolioDto(
        String symbolCode,
        String companyName,
        long quantity,             // تعداد سهم
        BigDecimal averageBuyPrice,    // میانگین قیمت خرید
        BigDecimal currentPrice,       // قیمت لحظه‌ای سهم
        BigDecimal currentValue,       // ارزش روز دارایی
        BigDecimal profitOrLoss,       // میزان سود یا زیان
        BigDecimal profitOrLossPercent // درصد سود یا زیان
) {
    public PortfolioDto(PortfolioItem item) {
        this(
                item.getSymbol().getCompanyName(),
                item.getSymbol().getCompanyName(), // نماد و نام شرکت یکی در نظر گرفته شده
                item.getQuantity(),
                item.getAveragePrice(),
                item.getSymbol().getUnitPrice(),
                // محاسبه ارزش روز: تعداد * قیمت لحظه‌ای
                new BigDecimal(item.getQuantity()).multiply(item.getSymbol().getUnitPrice()),
                // محاسبه سود و زیان کل
                calculateProfitOrLoss(item),
                // محاسبه درصد سود و زیان
                calculateProfitOrLossPercent(item)
        );
    }

    // متد کمکی برای محاسبه سود و زیان
    private static BigDecimal calculateProfitOrLoss(PortfolioItem item) {
        BigDecimal totalBuyValue = item.getAveragePrice().multiply(new BigDecimal(item.getQuantity()));
        BigDecimal currentTotalValue = item.getSymbol().getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
        return currentTotalValue.subtract(totalBuyValue);
    }

    // متد کمکی برای محاسبه درصد سود و زیان
    private static BigDecimal calculateProfitOrLossPercent(PortfolioItem item) {
        BigDecimal totalBuyValue = item.getAveragePrice().multiply(new BigDecimal(item.getQuantity()));
        if (totalBuyValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO; // برای جلوگیری از تقسیم بر صفر
        }
        BigDecimal profitOrLoss = calculateProfitOrLoss(item);
        // (سود/زیان تقسیم بر کل مبلغ خرید) * ۱۰۰
        return profitOrLoss.divide(totalBuyValue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
    }
}