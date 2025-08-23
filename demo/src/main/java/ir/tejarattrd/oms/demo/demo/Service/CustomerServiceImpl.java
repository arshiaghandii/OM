package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime; // برای ست کردن تاریخ ایجاد

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
        // **مهم: بررسی تکراری نبودن نام کاربری و ایمیل**
        customerRepository.findByUsernameOrEmail(customer.getUsername(), customer.getEmail())
                .ifPresent(existingCustomer -> {
                    throw new IllegalStateException("کاربری با این نام کاربری یا ایمیل از قبل وجود دارد.");
                });

        // هش کردن رمز عبور قبل از ذخیره
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        // ثبت زمان ایجاد کاربر
        customer.setCreatedAt(LocalDateTime.now());

        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer existingCustomer = getCustomerById(id);

        // بررسی اینکه ایمیل یا نام کاربری جدید به شخص دیگری تعلق نداشته باشد
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

        // فقط در صورتی که رمز عبور جدیدی ارسال شده باشد، آن را آپدیت کن
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
}