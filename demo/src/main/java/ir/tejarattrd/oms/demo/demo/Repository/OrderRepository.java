package ir.tejarattrd.oms.demo.demo.Repository;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // این متد برای سرویس پرتفوی لازم است
    List<Order> findByCustomer_UsernameAndOrderType(String username, String orderType);

    // ... (متدهای دیگری که ممکن است داشته باشید)

    // این خط را برای رفع خطا اضافه کنید
    List<Order> findByCustomerId(Long customerId);
}