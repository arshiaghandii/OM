package ir.tejarattrd.oms.demo.demo.Controller;


import ir.tejarattrd.oms.demo.demo.DTO.OrderRequestDto;
import ir.tejarattrd.oms.demo.demo.Service.TradingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final TradingService tradingService;

    public OrderController(TradingService tradingService) {
        this.tradingService = tradingService;
    }
    // STABILITY: Added @Valid to ensure incoming orders are validated.
    @PostMapping
    public ResponseEntity<?> submitOrder(@Valid @RequestBody OrderRequestDto orderRequest, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        String username = principal.getName();
        tradingService.placeOrder(username, orderRequest);
        return ResponseEntity.ok(Map.of("message", "Order placed successfully"));
    }
}