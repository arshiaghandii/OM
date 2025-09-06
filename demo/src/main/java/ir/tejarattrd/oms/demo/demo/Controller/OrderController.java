package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.DTO.OrderDto;
import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import ir.tejarattrd.oms.demo.demo.Repository.CustomerRepository;
import ir.tejarattrd.oms.demo.demo.Service.OrderService;
import ir.tejarattrd.oms.demo.demo.Service.SymbolService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final SymbolService symbolService;
    private final CustomerRepository customerRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public OrderController(OrderService orderService,
                           SymbolService symbolService,
                           CustomerRepository customerRepository,
                           SimpMessagingTemplate messagingTemplate) {
        this.orderService = orderService;
        this.symbolService = symbolService;
        this.customerRepository = customerRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public record OrderRequest(String symbolCode, String orderType, Double quantity) {}

    @GetMapping
    public ResponseEntity<List<OrderDto>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerRepository.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("کاربر پیدا نشد"));

        List<OrderDto> orderDtos = orderService.getOrdersByCustomerId(customer.getId())
                .stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderDtos);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Customer customer = customerRepository.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("کاربر برای ثبت سفارش پیدا نشد"));

            Symbol symbol = symbolService.getSymbolByCode(orderRequest.symbolCode());

            BigDecimal quantity = BigDecimal.valueOf(orderRequest.quantity());
            BigDecimal unitPrice = symbol.getUnitPrice();
            BigDecimal totalPrice = quantity.multiply(unitPrice);

            // **اصلاح کلیدی: بررسی موجودی در کنترلر قبل از ساخت سفارش**
            if ("buy".equalsIgnoreCase(orderRequest.orderType())) {
                if (customer.getBalance() == null || customer.getBalance().compareTo(totalPrice) < 0) {
                    throw new IllegalStateException("موجودی شما برای ثبت این سفارش خرید کافی نیست.");
                }
            }

            Order newOrder = new Order();
            newOrder.setCustomer(customer);
            newOrder.setSymbol(symbol);
            newOrder.setOrderType(orderRequest.orderType());
            newOrder.setQuantity(quantity);
            newOrder.setTotalPrice(totalPrice);
            newOrder.setStatus("pending");

            Order savedOrder = orderService.saveOrder(newOrder);
            OrderDto orderDto = new OrderDto(savedOrder);

            messagingTemplate.convertAndSend("/topic/new-order", orderDto);

            return ResponseEntity.ok(orderDto);
        } catch (IllegalStateException e) {
            // برگرداندن خطا با پیام مناسب به فرانت‌اند
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}