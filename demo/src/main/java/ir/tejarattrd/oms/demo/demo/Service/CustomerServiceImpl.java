package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

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
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @Override // متد save حالا استاندارد است
    public Customer saveCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer); // JPA خودش موجودیت ذخیره شده را برمی‌گرداند
    }

    @Override
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer existingCustomer = getCustomerById(id);
        existingCustomer.setFirst_name(customerDetails.getFirst_name());
        existingCustomer.setLast_name(customerDetails.getLast_name());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setPhone(customerDetails.getPhone());

        // اگر رمز عبور جدیدی ارسال شده بود، آن را هش کن
        if (customerDetails.getPassword() != null && !customerDetails.getPassword().isEmpty()) {
            existingCustomer.setPassword(passwordEncoder.encode(customerDetails.getPassword()));
        }

        return customerRepository.save(existingCustomer); // متد save برای آپدیت هم استفاده می‌شود
    }

    @Override
    public void deleteCustomer(Long id) {
        // ابتدا مطمئن شو که مشتری وجود دارد
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}