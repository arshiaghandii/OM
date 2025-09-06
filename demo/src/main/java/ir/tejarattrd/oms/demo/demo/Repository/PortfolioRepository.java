package ir.tejarattrd.oms.demo.demo.Repository;

import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioItem, Long> {
    // پیدا کردن یک آیتم پورتفولیو برای یک مشتری و یک نماد خاص
    Optional<PortfolioItem> findByCustomerIdAndSymbolSymbolCode(Long customerId, String symbolCode);
}