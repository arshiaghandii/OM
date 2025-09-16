package ir.tejarattrd.oms.demo.demo.Repository;

import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, Long> {
    /**
     * تمام آیتم‌های پورتفولیوی یک مشتری را پیدا می‌کند.
     */
    List<PortfolioItem> findByCustomerId(Long customerId);

    /**
     * یک آیتم خاص از پورتفولیوی مشتری را بر اساس نماد پیدا می‌کند.
     */
    Optional<PortfolioItem> findByCustomerIdAndSymbol(Long customerId, String symbol);
}
