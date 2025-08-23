package ir.tejarattrd.oms.demo.demo.Controller;
import ir.tejarattrd.oms.demo.demo.Service.CustomerService;
import ir.tejarattrd.oms.demo.demo.Entity.Customer;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:63342")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> registerCustomer(@Valid @RequestBody Customer customer) { // @Valid اضافه شد
        Customer savedCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.ok(savedCustomer);
    }
}