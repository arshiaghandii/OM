// Tejarat Project/demo/src/main/java/ir/tejarattrd/oms/demo/demo/Controller/OrderController.java

package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.DTO.OrderDto; // ایمپورت DTO جدید
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final SymbolService symbolService;
    private final CustomerRepository customerRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public OrderController(OrderService orderService, SymbolService symbolService, CustomerRepository customerRepository, SimpMessagingTemplate messagingTemplate) {
        this.orderService = orderService;
        this.symbolService = symbolService;
        this.customerRepository = customerRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // اندپوینت برای دریافت تمام سفارشات کاربر لاگین کرده
    // **نکته:** اینجا هم خروجی را به List<OrderDto> تبدیل می‌کنیم تا ایمن‌تر باشد
    @GetMapping
    public ResponseEntity<List<OrderDto>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerRepository.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("کاربر پیدا نشد"));

        List<Order> orders = orderService.getOrdersByCustomerId(customer.getId());
        List<OrderDto> orderDtos = orders.stream().map(OrderDto::new).collect(Collectors.toList());

        return ResponseEntity.ok(orderDtos);
    }

    // اندپوینت برای ثبت سفارش جدید
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderRequest orderRequest, @AuthenticationPrincipal UserDetails userDetails) {
        // پیدا کردن کاربر فعلی
        Customer customer = customerRepository.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("کاربر برای ثبت سفارش پیدا نشد"));

        // پیدا کردن نماد
        Symbol symbol = symbolService.getSymbolByCode(orderRequest.getSymbolCode());

        // ساخت آبجکت سفارش
        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        newOrder.setSymbol(symbol);
        newOrder.setOrderType(orderRequest.getOrderType());
        newOrder.setQuantity(orderRequest.getQuantity());
        newOrder.setTotalPrice(orderRequest.getQuantity() * symbol.getUnitPrice()); // قیمت کل در سرور محاسبه شود
        newOrder.setStatus("pending"); // وضعیت اولیه

        Order savedOrder = orderService.saveOrder(newOrder);

        // **تغییر کلیدی: تبدیل Entity به DTO قبل از ارسال**
        OrderDto orderDto = new OrderDto(savedOrder);

        // ارسال DTO به جای Entity از طریق وب‌سوکت
        messagingTemplate.convertAndSend("/topic/new-order", orderDto);

        // برگرداندن DTO به عنوان پاسخ درخواست HTTP
        return ResponseEntity.ok(orderDto);
    }

    // یک DTO ساده برای دریافت درخواست ثبت سفارش
    public static class OrderRequest {
        private String symbolCode;
        private String orderType;
        private Double quantity;

        // Getters and Setters
        public String getSymbolCode() { return symbolCode; }
        public void setSymbolCode(String symbolCode) { this.symbolCode = symbolCode; }
        public String getOrderType() { return orderType; }
        public void setOrderType(String orderType) { this.orderType = orderType; }
        public Double getQuantity() { return quantity; }
        public void setQuantity(Double quantity) { this.quantity = quantity; }
    }
}