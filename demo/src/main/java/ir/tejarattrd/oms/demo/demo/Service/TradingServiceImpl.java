package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Core.MatchingEngine;
import ir.tejarattrd.oms.demo.demo.Core.OrderBook;
import ir.tejarattrd.oms.demo.demo.Core.MatchResult;
import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Repository.OrderRepository;
import ir.tejarattrd.oms.demo.demo.Repository.TradeRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TradingServiceImpl implements TradingService {

    private static final Logger logger = LoggerFactory.getLogger(TradingServiceImpl.class);



    // فیلدهای final توسط Lombok در سازنده قرار می‌گیرند
    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;

    public TradingServiceImpl(OrderRepository orderRepository, TradeRepository tradeRepository) {
        this.orderRepository = orderRepository;
        this.tradeRepository = tradeRepository;
    }

    private final Map<String, OrderBook> orderBooks = new ConcurrentHashMap<>();
    private final Map<String, MatchingEngine> matchingEngines = new ConcurrentHashMap<>();

    /*
     * سازنده دستی حذف شد چون @RequiredArgsConstructor همین کار را انجام می‌دهد.
     * public TradingServiceImpl(OrderRepository orderRepository, TradeRepository tradeRepository) {
     * this.orderRepository = orderRepository;
     * this.tradeRepository = tradeRepository;
     * }
     */

    @PostConstruct
    public void init() {
        logger.info("در حال بارگذاری سفارشات باز در حافظه...");
        List<Order> openOrders = orderRepository.findByStatusIn(
                List.of(Order.OrderStatus.OPEN, Order.OrderStatus.PARTIALLY_FILLED)
        );
        openOrders.forEach(order -> {
            OrderBook book = orderBooks.computeIfAbsent(order.getSymbol().getCompanyName(), s -> new OrderBook());
            book.addOrder(order);
        });
        logger.info("{} عدد سفارش باز بارگذاری شد.", openOrders.size());
    }

    @Override
    @Transactional
    public Order placeNewOrder(Order order) {
        logger.info("در حال پردازش سفارش جدید: {} {} {} @ {}", order.getSide(), order.getQuantity(), order.getSymbol().getCompanyName(), order.getPrice());
        Order savedOrder = orderRepository.save(order);

        MatchingEngine engine = matchingEngines.computeIfAbsent(savedOrder.getSymbol().getCompanyName(), symbol -> {
            OrderBook book = orderBooks.computeIfAbsent(symbol, s -> new OrderBook());
            return new MatchingEngine(book);
        });

        MatchResult result = engine.processOrder(savedOrder);

        if (!result.getTrades().isEmpty()) {
            tradeRepository.saveAll(result.getTrades());
            logger.info("{} عدد معامله انجام شد.", result.getTrades().size());
        }
        if (!result.getUpdatedOrders().isEmpty()) {
            orderRepository.saveAll(result.getUpdatedOrders());
        }
        return savedOrder;
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order.getStatus() != Order.OrderStatus.OPEN && order.getStatus() != Order.OrderStatus.PARTIALLY_FILLED) {
            throw new IllegalStateException("فقط سفارشات باز یا نیمه پر شده قابل لغو هستند.");
        }
        OrderBook book = orderBooks.get(order.getSymbol().getCompanyName());
        if (book != null) {
            book.removeOrder(order);
        }
        order.setStatus(Order.OrderStatus.CANCELED);
        orderRepository.save(order);
        logger.info("سفارش با شناسه {} لغو شد.", orderId);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("سفارشی با شناسه " + id + " یافت نشد."));
    }
}