package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.DTO.OrderDto;
import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;
import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import ir.tejarattrd.oms.demo.demo.Repository.OrderRepository;
import ir.tejarattrd.oms.demo.demo.Repository.PortfolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(OrderProcessingService.class);

    private final OrderRepository orderRepository;
    private final PortfolioRepository portfolioRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public OrderProcessingService(OrderRepository orderRepository,
                                  PortfolioRepository portfolioRepository,
                                  SimpMessagingTemplate messagingTemplate) {
        this.orderRepository = orderRepository;
        this.portfolioRepository = portfolioRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * این متد هر ۵ ثانیه یک بار اجرا می‌شود تا سفارشات در حال انتظار را پردازش کند.
     */
    @Scheduled(fixedRate = 5000)
    @Transactional
    public void processPendingOrders() {
        List<Order> pendingOrders = orderRepository.findByStatus("pending");
        if (pendingOrders.isEmpty()) {
            return;
        }
        logger.info("Found {} pending orders to process.", pendingOrders.size());

        for (Order order : pendingOrders) {
            try {
                if ("buy".equalsIgnoreCase(order.getOrderType())) {
                    executeBuyOrder(order);
                } else if ("sell".equalsIgnoreCase(order.getOrderType())) {
                    executeSellOrder(order);
                }
            } catch (Exception e) {
                logger.error("Failed to process order {}: {}", order.getOrderId(), e.getMessage());
                order.setStatus("failed"); // تغییر وضعیت سفارش به ناموفق
                Order savedOrder = orderRepository.save(order);
                // ارسال آپدیت وضعیت به کلاینت
                messagingTemplate.convertAndSend("/topic/order-update", new OrderDto(savedOrder));
            }
        }
    }

    /**
     * منطق اجرای سفارش خرید.
     */
    private void executeBuyOrder(Order order) {
        Customer customer = order.getCustomer();
        Symbol symbol = order.getSymbol();
        BigDecimal cost = order.getTotalPrice();

        if (customer.getBalance().compareTo(cost) < 0) {
            throw new IllegalStateException("Insufficient funds.");
        }
        // کسر هزینه از حساب مشتری
        customer.setBalance(customer.getBalance().subtract(cost));

        // پیدا کردن یا ایجاد آیتم پورتفولیو برای مشتری و نماد
        PortfolioItem portfolioItem = portfolioRepository
                .findByCustomerIdAndSymbolSymbolCode(customer.getId(), symbol.getSymbolCode())
                .orElseGet(() -> {
                    PortfolioItem newItem = new PortfolioItem();
                    newItem.setCustomer(customer);
                    newItem.setSymbol(symbol);
                    newItem.setQuantity(BigDecimal.ZERO); // مقدار اولیه BigDecimal
                    return newItem;
                });

        // اضافه کردن تعداد سهام خریداری شده به پورتفولیو
        portfolioItem.setQuantity(portfolioItem.getQuantity().add(order.getQuantity()));
        portfolioRepository.save(portfolioItem);

        // تغییر وضعیت سفارش به تکمیل شده
        order.setStatus("completed");
        Order savedOrder = orderRepository.save(order);
        logger.info("Successfully executed buy order {}.", order.getOrderId());

        // ارسال آپدیت وضعیت به کلاینت
        messagingTemplate.convertAndSend("/topic/order-update", new OrderDto(savedOrder));
    }

    /**
     * منطق اجرای سفارش فروش.
     */
    // آدرس فایل: Tejarat Project/demo/src/main/java/ir/tejarattrd/oms/demo/demo/Service/OrderProcessingService.java

    private void executeSellOrder(Order order) {
        Customer customer = order.getCustomer();
        Symbol symbol = order.getSymbol();
        BigDecimal proceeds = order.getTotalPrice();

        PortfolioItem portfolioItem = portfolioRepository
                .findByCustomerIdAndSymbolSymbolCode(customer.getId(), symbol.getSymbolCode())
                .orElseThrow(() -> new IllegalStateException("Stock not found in portfolio."));

        if (portfolioItem.getQuantity().compareTo(order.getQuantity()) < 0) {
            throw new IllegalStateException("Insufficient stock quantity.");
        }

        // محاسبه تعداد جدید سهام
        BigDecimal newQuantity = portfolioItem.getQuantity().subtract(order.getQuantity());

        // اگر تعداد سهم صفر شد، ردیف را حذف کن
        if (newQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            portfolioRepository.delete(portfolioItem);
            logger.info("Portfolio item for symbol {} deleted for customer {}.", symbol.getSymbolCode(), customer.getId());
        } else { // در غیر این صورت، فقط تعداد جدید را ذخیره کن
            portfolioItem.setQuantity(newQuantity);
            portfolioRepository.save(portfolioItem);
        }

        // اضافه کردن مبلغ حاصل از فروش به حساب مشتری
        customer.setBalance(customer.getBalance().add(proceeds));

        // تغییر وضعیت سفارش به تکمیل شده
        order.setStatus("completed");
        Order savedOrder = orderRepository.save(order);
        logger.info("Successfully executed sell order {}.", order.getOrderId());

        // ارسال آپدیت وضعیت به کلاینت
        messagingTemplate.convertAndSend("/topic/order-update", new OrderDto(savedOrder));

        // **مهم:** ارسال پیام برای آپدیت لحظه‌ای پورتفولیو در فرانت‌اند
        messagingTemplate.convertAndSendToUser(
                customer.getUsername(),
                "/queue/portfolio", // این یک صف شخصی برای هر کاربر است
                "update"
        );
    }
}