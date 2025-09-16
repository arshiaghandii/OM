package ir.tejarattrd.oms.demo.demo.DTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * FIX: This DTO has been redesigned to represent the entire portfolio.
 * It now correctly holds the customer's balance and a list of their portfolio items (stocks).
 * This resolves the structural mismatch that was causing the compilation error.
 * The original record, which represented a single item, was functionally a duplicate of PortfolioItemDto.
 */
public record PortfolioResponseDto(
        BigDecimal balance,
        List<PortfolioItemDto> items
) {
}