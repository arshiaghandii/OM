package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.DTO.SymbolDto;
import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import ir.tejarattrd.oms.demo.demo.Service.SymbolService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@EnableScheduling
public class SymbolController {

    private final SymbolService symbolService;
    private final SimpMessagingTemplate messagingTemplate;
    // زمان به‌روزرسانی قیمت‌ها (هر ۲ ثانیه)
    private static final long UPDATE_TIME_MS = 2000;

    public SymbolController(SymbolService symbolService, SimpMessagingTemplate messagingTemplate) {
        this.symbolService = symbolService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * دریافت لیست تمام نمادهای موجود.
     */
    @GetMapping("/api/symbols")
    public ResponseEntity<List<SymbolDto>> getAllSymbols() {
        List<SymbolDto> symbolDtos = symbolService.getAllSymbols()
                .stream()
                .map(SymbolDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(symbolDtos);
    }

    /**
     * ایجاد یک نماد جدید (معمولاً توسط ادمین استفاده می‌شود).
     */
    @PostMapping("/api/symbols")
    public ResponseEntity<SymbolDto> createSymbol(@RequestBody Symbol symbol) {
        Symbol savedSymbol = symbolService.saveSymbol(symbol);
        SymbolDto symbolDto = new SymbolDto(savedSymbol);
        // ارسال پیام به کلاینت‌ها برای اضافه شدن نماد جدید
        messagingTemplate.convertAndSend("/topic/new-symbol", symbolDto);
        return ResponseEntity.ok(symbolDto);
    }

    /**
     * این متد به صورت زمان‌بندی شده اجرا می‌شود تا قیمت یک نماد را به صورت تصادفی
     * تغییر داده و برای تمام کلاینت‌های متصل از طریق WebSocket ارسال کند.
     */
    @Scheduled(fixedRate = UPDATE_TIME_MS)
    public void updateAndBroadcastSymbolPrices() {
        List<Symbol> symbols = symbolService.getAllSymbols();
        if (symbols.isEmpty()) {
            return;
        }

        Random random = new Random();
        Symbol symbolToUpdate = symbols.get(random.nextInt(symbols.size()));

        // **منطق آپدیت قیمت (برای تست)**
        BigDecimal currentPrice = symbolToUpdate.getUnitPrice() != null ? symbolToUpdate.getUnitPrice() : BigDecimal.ZERO;
        double changePercentage = (random.nextDouble() - 0.5) * 0.02; // تغییر بین -1% و +1%
        BigDecimal changeAmount = currentPrice.multiply(BigDecimal.valueOf(changePercentage));
        BigDecimal newPrice = currentPrice.add(changeAmount).setScale(2, RoundingMode.HALF_UP); // گرد کردن به دو رقم اعشار

        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            newPrice = BigDecimal.ZERO;
        }

        // آپدیت قیمت و حجم معاملات در سرویس
        symbolToUpdate.setUnitPrice(newPrice);
        Long newVolume = (symbolToUpdate.getTradingVolume() != null ? symbolToUpdate.getTradingVolume() : 0L) + random.nextInt(10000);
        symbolToUpdate.setTradingVolume(newVolume);

        // **مهم: از متد آپدیت ترکیبی در سرویس استفاده می‌کنیم**
        symbolService.updateSymbol(symbolToUpdate.getSymbolId(), symbolToUpdate);

        // ارسال DTO آپدیت شده
        Symbol updatedSymbolEntity = symbolService.getSymbolById(symbolToUpdate.getSymbolId());
        SymbolDto symbolDto = new SymbolDto(updatedSymbolEntity);
        messagingTemplate.convertAndSend("/topic/symbols", symbolDto);
    }
}