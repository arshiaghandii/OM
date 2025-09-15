package ir.tejarattrd.oms.demo.demo.DTO;

import ir.tejarattrd.oms.demo.demo.Entity.Symbol;


public class SymbolDto {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Long id;
    private String name;
    private String description;

    public static SymbolDto fromEntity(Symbol symbol) {
        SymbolDto dto = new SymbolDto();
        dto.setId(symbol.getId());
        dto.setName(symbol.getName());
        dto.setDescription(symbol.getDescription());
        return dto;
    }
}