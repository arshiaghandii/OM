package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import ir.tejarattrd.oms.demo.demo.Repository.SymbolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SymbolServiceImpl implements SymbolService {

    private static final Logger logger = LoggerFactory.getLogger(SymbolServiceImpl.class);

    private final SymbolRepository symbolRepository;

    public SymbolServiceImpl(SymbolRepository symbolRepository) {
        this.symbolRepository = symbolRepository;
    }

    @Override
    public List<Symbol> getAllSymbols() {
        logger.info("Fetching all symbols");
        return symbolRepository.findAll();
    }

    @Override
    public Symbol getSymbolById(Long id) {
        logger.info("Fetching symbol with id: {}", id);
        return symbolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("نمادی با شناسه " + id + " پیدا نشد"));
    }

    @Override
    public Symbol getSymbolByCode(String code) {
        logger.info("Fetching symbol with code: {}", code);
        return symbolRepository.findBySymbolCode(code)
                .orElseThrow(() -> new RuntimeException("نمادی با کد " + code + " پیدا نشد"));
    }

    @Override
    public Symbol saveSymbol(Symbol symbol) {
        logger.info("Saving new symbol with code: {}", symbol.getSymbolCode());
        symbol.setLastPriceUpdate(LocalDateTime.now());
        return symbolRepository.save(symbol);
    }

    @Override
    public Symbol updateSymbol(Long id, Symbol symbolDetails) {
        logger.info("Updating symbol with id: {}", id);
        Symbol existingSymbol = getSymbolById(id);

        existingSymbol.setCompanyName(symbolDetails.getCompanyName());
        existingSymbol.setMarket(symbolDetails.getMarket());
        existingSymbol.setUnitPrice(symbolDetails.getUnitPrice());
        // **مهم: حجم معاملات هم در این متد آپدیت می‌شود**
        if (symbolDetails.getTradingVolume() != null) {
            existingSymbol.setTradingVolume(symbolDetails.getTradingVolume());
        }
        existingSymbol.setLastPriceUpdate(LocalDateTime.now());

        return symbolRepository.save(existingSymbol);
    }

    @Override
    public Symbol updateSymbolVolume(Long id, Long newVolume) {
        Symbol existingSymbol = getSymbolById(id);
        existingSymbol.setTradingVolume(newVolume);
        existingSymbol.setLastPriceUpdate(LocalDateTime.now());
        return symbolRepository.save(existingSymbol);
    }

    /**
     * این متد فقط قیمت یک نماد را بر اساس شناسه آن به‌روزرسانی می‌کند.
     */
    @Override
    public Symbol updateSymbolPrice(Long id, BigDecimal newPrice) {
        Symbol existingSymbol = getSymbolById(id);
        existingSymbol.setUnitPrice(newPrice);
        existingSymbol.setLastPriceUpdate(LocalDateTime.now());
        return symbolRepository.save(existingSymbol);
    }

    @Override
    public void deleteSymbol(Long id) {
        logger.info("Deleting symbol with id: {}", id);
        if (!symbolRepository.existsById(id)) {
            logger.error("Failed to delete symbol with id: {}. Not found.", id);
            throw new RuntimeException("نمادی برای حذف با شناسه " + id + " پیدا نشد");
        }
        symbolRepository.deleteById(id);
        logger.info("Symbol deleted successfully: id={}", id);
    }
}