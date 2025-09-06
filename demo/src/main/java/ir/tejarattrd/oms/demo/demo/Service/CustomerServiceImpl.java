package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("مشتری با شناسه " + id + " پیدا نشد"));
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        customerRepository.findByUsernameOrEmail(customer.getUsername(), customer.getEmail())
                .ifPresent(existingCustomer -> {
                    throw new IllegalStateException("کاربری با این نام کاربری یا ایمیل از قبل وجود دارد.");
                });
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setCreatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer existingCustomer = getCustomerById(id);
        customerRepository.findByUsernameOrEmail(customerDetails.getUsername(), customerDetails.getEmail())
                .ifPresent(anotherCustomer -> {
                    if (!anotherCustomer.getId().equals(id)) {
                        throw new IllegalStateException("نام کاربری یا ایمیل جدید به کاربر دیگری تعلق دارد.");
                    }
                });
        existingCustomer.setFirst_name(customerDetails.getFirst_name());
        existingCustomer.setLast_name(customerDetails.getLast_name());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setUsername(customerDetails.getUsername());
        existingCustomer.setPhone(customerDetails.getPhone());
        if (customerDetails.getPassword() != null && !customerDetails.getPassword().trim().isEmpty()) {
            existingCustomer.setPassword(passwordEncoder.encode(customerDetails.getPassword()));
        }
        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("مشتری برای حذف با شناسه " + id + " پیدا نشد");
        }
        customerRepository.deleteById(id);
    }

    // متد زیر برای تطابق با اینترفیس کامنت می‌شود
    // @Override
    // public Customer getCustomerByUsername(String username) {
    //     return null;
    // }
}