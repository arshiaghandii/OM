package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Repository.CustomerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // **اصلاح کلیدی:** ورودی usernameOrEmail به عنوان هر دو پارامتر ارسال می‌شود
        Customer customer = customerRepository
                .findByUsername(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("کاربری با این نام کاربری یا ایمیل پیدا نشد"));

        return User.builder()
                .username(customer.getUsername())
                .password(customer.getPassword())
                .roles("USER")
                .build();
    }
}