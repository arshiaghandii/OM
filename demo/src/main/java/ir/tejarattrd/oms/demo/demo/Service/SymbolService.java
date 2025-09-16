package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import java.math.BigDecimal;
import java.util.List;

public interface SymbolService {

    /**
     * دریافت لیست تمام نمادها.
     */
    List<Symbol> getAllSymbols();

    /**
     * پیدا کردن یک نماد بر اساس شناسه (ID).
     */
    Symbol getSymbolById(Long id);

    /**
     * پیدا کردن یک نماد بر اساس کد آن (مثلاً "VABM").
     */
    Symbol getSymbolByCode(String code);

    /**
     * ذخیره یک نماد جدید.
     */
    Symbol saveSymbol(Symbol symbol);

    /**
     * به‌روزرسانی اطلاعات کلی یک نماد.
     */
    Symbol updateSymbol(Long id, Symbol symbol);

    /**
     * حذف یک نماد بر اساس شناسه.
     */
    void deleteSymbol(Long id);

    /**
     * به‌روزرسانی حجم معاملات یک نماد خاص.
     */
    Symbol updateSymbolVolume(Long id, Long newVolume);

    /**
     * به‌روزرسانی قیمت یک نماد خاص.
     */
    Symbol updateSymbolPrice(Long id, BigDecimal newPrice);
}