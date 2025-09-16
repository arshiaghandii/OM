package ir.tejarattrd.oms.demo.demo.Repository;

import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, Long> {

    // این متد برای گرفتن کل پورتفولیوی یک مشتری استفاده می‌شود
    List<PortfolioItem> findByCustomerId(Long customerId);

    // FIX: متد مورد نیاز برای پیدا کردن یک آیتم خاص در پورتفolio اضافه شد
    // این متد یک آیتم را بر اساس شناسه مشتری و شناسه نماد پیدا می‌کند
    Optional<PortfolioItem> findByCustomerIdAndSymbolId(Long customerId, Long symbolId);

    // این متد قدیمی بود و دیگر استفاده نمی‌شود، اما برای کامل بودن آن را نگه می‌داریم
    Optional<PortfolioItem> findByCustomerIdAndSymbol(Long customerId, String companyName);
}