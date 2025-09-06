package ir.tejarattrd.oms.demo.demo.Repository;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * پیدا کردن تمام سفارشات یک مشتری خاص.
     */
    List<Order> findByCustomerId(Long customerId);

    /**
     * پیدا کردن تمام سفارشات با یک وضعیت خاص (مثلاً "pending").
     */
    List<Order> findByStatus(String status);
}