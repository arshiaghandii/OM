package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.DTO.OrderRequestDto;
import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import ir.tejarattrd.oms.demo.demo.Repository.CustomerRepository;
import ir.tejarattrd.oms.demo.demo.Repository.SymbolRepository;
import ir.tejarattrd.oms.demo.demo.Service.TradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final TradingService tradingService;
    private final CustomerRepository customerRepository;
    private final SymbolRepository symbolRepository;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto orderRequest) {
        // ۱. مشتری و نماد را از دیتابیس پیدا می‌کنیم
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Symbol symbol = symbolRepository.findById(orderRequest.getSymbolId())
                .orElseThrow(() -> new RuntimeException("Symbol not found"));

        // ۲. یک موجودیت Order جدید می‌سازیم
        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        newOrder.setSymbol(symbol);
        newOrder.setSide(orderRequest.getSide());
        newOrder.setQuantity(orderRequest.getQuantity());
        newOrder.setPrice(orderRequest.getPrice());
        // وضعیت اولیه به صورت خودکار در موجودیت ست می‌شود

        // ۳. سفارش را برای پردازش به هسته معاملاتی ارسال می‌کنیم
        Order processedOrder = tradingService.placeNewOrder(newOrder);

        return ResponseEntity.ok(processedOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(tradingService.getOrderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        tradingService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}