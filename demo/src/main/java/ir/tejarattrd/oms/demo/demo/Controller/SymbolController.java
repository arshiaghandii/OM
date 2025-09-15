// Tejarat Project/demo/src/main/java/ir/tejarattrd/oms/demo/demo/Controller/SymbolController.java

package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.DTO.SymbolDto;
import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import ir.tejarattrd.oms.demo.demo.Service.SymbolService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@EnableScheduling
public class SymbolController {

    private final SymbolService symbolService;
    private final SimpMessagingTemplate messagingTemplate;
    private static final int UPDATE_TIME_MS = 1000;

    public SymbolController(SymbolService symbolService, SimpMessagingTemplate messagingTemplate) {
        this.symbolService = symbolService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/api/symbols")
    public ResponseEntity<List<SymbolDto>> getAllSymbols() {
        List<SymbolDto> symbolDtos = symbolService.getAllSymbols()
                .stream()
                // With the new constructor, this now works perfectly.
                .map(SymbolDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(symbolDtos);
    }

    @PostMapping("/api/symbols")
    public ResponseEntity<SymbolDto> createSymbol(@RequestBody Symbol symbol) {
        Symbol savedSymbol = symbolService.saveSymbol(symbol);

        // This also works now.
        SymbolDto symbolDto = new SymbolDto(savedSymbol);
        messagingTemplate.convertAndSend("/topic/new-symbol", symbolDto);

        return ResponseEntity.ok(symbolDto);
    }

    @Scheduled(fixedRate = UPDATE_TIME_MS)
    public void updateAndBroadcastSymbolPrices() {
        List<Symbol> symbols = symbolService.getAllSymbols();
        if (symbols.isEmpty()) {
            return;
        }

        Random random = new Random();
        Symbol symbolToUpdate = symbols.get(random.nextInt(symbols.size()));

        long currentVolume = (symbolToUpdate.getTradingVolume() != null) ? symbolToUpdate.getTradingVolume() : 0L;
        double change = (random.nextDouble() - 0.5) * (currentVolume * 0.02);
        long newTradingVolume = Math.round(currentVolume + change);

        // --- FIX: Changed getSymbolId() to getId() ---
        symbolService.updateSymbolVolume(symbolToUpdate.getId(), newTradingVolume);
        Symbol updatedSymbolEntity = symbolService.getSymbolById(symbolToUpdate.getId());
        // ---------------------------------------------

        // And this works too.
        SymbolDto symbolDto = new SymbolDto(updatedSymbolEntity);

        messagingTemplate.convertAndSend("/topic/symbols", symbolDto);
    }
}