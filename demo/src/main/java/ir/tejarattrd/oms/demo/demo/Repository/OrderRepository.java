package ir.tejarattrd.oms.demo.demo.Repository;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.symbol = :symbol AND o.side = 'BUY' AND o.status IN ('OPEN', 'PARTIALLY_FILLED') ORDER BY o.price DESC, o.createdAt ASC")
    List<Order> findBids(@Param("symbol") String symbol);

    @Query("SELECT o FROM Order o WHERE o.symbol = :symbol AND o.side = 'SELL' AND o.status IN ('OPEN', 'PARTIALLY_FILLED') ORDER BY o.price ASC, o.createdAt ASC")
    List<Order> findAsks(@Param("symbol") String symbol);

    // STABILITY: Added a query to find all orders that need processing.
    @Query("SELECT o FROM Order o WHERE o.status IN ('OPEN', 'PARTIALLY_FILLED')")
    List<Order> findUnfilledOrders();

    List<Order> findByStatusIn(Collection<Order.OrderStatus> statuses);

}
