package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import ir.tejarattrd.oms.demo.demo.Service.SymbolService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@EnableScheduling
public class SymbolController {

    private final SymbolService symbolService;
    private final SimpMessagingTemplate messagingTemplate;
    private static final int UPDATE_TIME_MS = 1000;  // 1000 milliseconds


    public SymbolController(SymbolService symbolService, SimpMessagingTemplate messagingTemplate) {
        this.symbolService = symbolService;
        this.messagingTemplate = messagingTemplate;
    }

    // ... متدهای getAllSymbols و createSymbol بدون تغییر ...
    @GetMapping("/api/symbols")
    public ResponseEntity<List<Symbol>> getAllSymbols() {
        return ResponseEntity.ok(symbolService.getAllSymbols());
    }

    @PostMapping("/api/symbols")
    public ResponseEntity<Symbol> createSymbol(@RequestBody Symbol symbol) {
        Symbol savedSymbol = symbolService.saveSymbol(symbol);
        System.out.println("Broadcasting new symbol: " + savedSymbol.getSymbolCode());
        messagingTemplate.convertAndSend("/topic/new-symbol", savedSymbol);
        return ResponseEntity.ok(savedSymbol);
    }


    @Scheduled(fixedRate = UPDATE_TIME_MS)
    public void updateAndBroadcastSymbolPrices() {
        List<Symbol> symbols = symbolService.getAllSymbols();
        if (symbols.isEmpty()) {
            return;
        }

        Random random = new Random();
        Symbol symbolToUpdate = symbols.get(random.nextInt(symbols.size()));

        // --- به‌روزرسانی قیمت (منطق قبلی شما برای تغییر قیمت) ---
        // فرض می‌کنیم منطق قیمت بر اساس حجم معاملات است
        long currentVolume = (symbolToUpdate.getTradingVolume() != null) ? symbolToUpdate.getTradingVolume() : 0L;
        double change = (random.nextDouble() - 0.5) * (currentVolume * 0.02); // تغییر ۲۰ درصدی رندوم
        long newTradingVolume = Math.round(currentVolume + change);

        // به‌روزرسانی حجم معاملات در دیتابیس
        symbolService.updateSymbolVolume(symbolToUpdate.getSymbolId(), newTradingVolume);

        // دریافت نماد به‌روز شده برای ارسال به کلاینت
        Symbol updatedSymbol = symbolService.getSymbolById(symbolToUpdate.getSymbolId());
        messagingTemplate.convertAndSend("/topic/symbols", updatedSymbol);
    }
}