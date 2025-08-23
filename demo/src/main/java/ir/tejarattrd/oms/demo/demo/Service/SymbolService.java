package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Symbol;

import java.util.List;

public interface SymbolService {
    List<Symbol> getAllSymbols();
    Symbol getSymbolById(Long id);
    Symbol getSymbolByCode(String code);
    Symbol saveSymbol(Symbol symbol);
    Symbol updateSymbol(Long id, Symbol symbol);
    void deleteSymbol(Long id);
}
