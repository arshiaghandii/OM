package ir.tejarattrd.oms.demo.demo.Repository;

import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * پیدا کردن مشتری بر اساس نام کاربری یا ایمیل.
     * Spring Data JPA به صورت خودکار کوئری این متد را از روی نام آن تولید می‌کند.
     */
    Optional<Customer> findByUsernameOrEmail(String username, String email);
}