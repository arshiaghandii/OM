package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.DTO.LoginForm;
import ir.tejarattrd.oms.demo.demo.DTO.RegisterForm;
import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // STABILITY: Method now accepts a RegisterForm DTO instead of the Customer entity.
    public void register(RegisterForm registerForm) {
        if (customerRepository.findByUsername(registerForm.getUsername()).isPresent()) {
            throw new IllegalStateException("Username is already taken");
        }

        Customer customer = new Customer();
        customer.setUsername(registerForm.getUsername());
        customer.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        // Set an initial balance for new users
        customer.setBalance(new BigDecimal("1000000.00"));

        customerRepository.save(customer);
    }

    public String login(LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsernameOrEmail(), loginForm.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtil.generateToken(authentication.getName());
    }
}
