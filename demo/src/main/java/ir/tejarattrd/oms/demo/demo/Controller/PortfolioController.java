package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.DTO.PortfolioDto;
import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Repository.CustomerRepository;
import ir.tejarattrd.oms.demo.demo.Service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final CustomerRepository customerRepository;

    public PortfolioController(PortfolioService portfolioService, CustomerRepository customerRepository) {
        this.portfolioService = portfolioService;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public ResponseEntity<List<PortfolioDto>> getMyPortfolio(@AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerRepository.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("کاربر پیدا نشد"));

        List<PortfolioDto> portfolio = portfolioService.getPortfolioByCustomerId(customer.getId());
        return ResponseEntity.ok(portfolio);
    }
}