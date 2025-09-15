package ir.tejarattrd.oms.demo.demo.Repository;

import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, Long> {
    List<PortfolioItem> findByCustomer_Username(String username);
}