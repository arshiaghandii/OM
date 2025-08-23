package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String loginAndGetToken(String usernameOrEmail, String rawPassword) {
        // پیدا کردن کاربر با یوزرنیم یا ایمیل
        Customer customer = customerRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("کاربری با این نام یا ایمیل وجود ندارد"));

        // بررسی پسورد
        if (!passwordEncoder.matches(rawPassword, customer.getPassword())) {
            throw new RuntimeException("رمز عبور اشتباه است");
        }

        // تولید JWT
        return jwtUtil.generateToken(customer.getUsername());
    }
}
