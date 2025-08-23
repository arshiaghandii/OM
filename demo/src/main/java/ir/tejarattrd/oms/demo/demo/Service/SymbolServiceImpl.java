package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import ir.tejarattrd.oms.demo.demo.Repository.SymbolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> {
                    logger.error("Symbol not found with id: {}", id);
                    return new RuntimeException("Symbol not found with id: " + id);
                });
    }

    @Override
    public Symbol getSymbolByCode(String code) {
        logger.info("Fetching symbol with code: {}", code);
        return symbolRepository.findBySymbolCode(code)
                .orElseThrow(() -> {
                    logger.error("Symbol not found with code: {}", code);
                    return new RuntimeException("Symbol not found with code: " + code);
                });
    }

    @Override
    public Symbol saveSymbol(Symbol symbol) {
        logger.info("Saving new symbol with code: {}", symbol.getSymbolCode());

        // مقداردهی lastPriceUpdate هنگام ذخیره اولیه
        symbol.setLastPriceUpdate(LocalDateTime.now());

        int result = symbolRepository.save(symbol);
        if (result > 0) {
            logger.info("Symbol saved successfully: {}", symbol.getSymbolCode());
            return symbol;
        }

        logger.error("Failed to save symbol: {}", symbol.getSymbolCode());
        throw new RuntimeException("Failed to save symbol with code: " + symbol.getSymbolCode());
    }

    @Override
    public Symbol updateSymbol(Long id, Symbol symbol) {
        logger.info("Updating symbol with id: {}", id);
        Symbol existing = getSymbolById(id);

        existing.setCompanyName(symbol.getCompanyName());
        existing.setMarket(symbol.getMarket());
        existing.setUnitPrice(symbol.getUnitPrice());

        // به‌روزرسانی زمان آخرین تغییر قیمت
        existing.setLastPriceUpdate(LocalDateTime.now());

        int result = symbolRepository.update(existing);
        if (result > 0) {
            logger.info("Symbol updated successfully: id={}", id);
            return existing;
        }

        logger.error("Failed to update symbol with id: {}", id);
        throw new RuntimeException("Failed to update symbol with id: " + id);
    }

    @Override
    public void deleteSymbol(Long id) {
        logger.info("Deleting symbol with id: {}", id);
        int result = symbolRepository.deleteById(id);
        if (result == 0) {
            logger.error("Failed to delete symbol with id: {}", id);
            throw new RuntimeException("Failed to delete symbol with id: " + id);
        }
        logger.info("Symbol deleted successfully: id={}", id);
    }
}
