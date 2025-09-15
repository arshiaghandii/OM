package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Core.MatchingEngine;
import ir.tejarattrd.oms.demo.demo.Core.OrderBook;
import ir.tejarattrd.oms.demo.demo.Core.MatchResult;
import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Repository.OrderRepository;
import ir.tejarattrd.oms.demo.demo.Repository.TradeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TradingServiceImpl implements OrderService { // یا یک اینترفیس جدید

    private static final Logger logger = LoggerFactory.getLogger(TradingServiceImpl.class);
    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;

    private final Map<String, OrderBook> orderBooks = new ConcurrentHashMap<>();
    private final Map<String, MatchingEngine> matchingEngines = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        logger.info("Loading open orders into memory...");
        List<Order> openOrders = orderRepository.findByStatusIn(
                List.of(Order.OrderStatus.OPEN, Order.OrderStatus.PARTIALLY_FILLED)
        );
        openOrders.forEach(order -> {
            OrderBook book = orderBooks.computeIfAbsent(order.getSymbol().getName(), s -> new OrderBook());
            book.addOrder(order);
        });
        logger.info("Loaded {} open orders.", openOrders.size());
    }

    @Transactional
    public Order placeNewOrder(Order order) {
        logger.info("Processing new order: {} {} {} @ {}", order.getSide(), order.getQuantity(), order.getSymbol().getName(), order.getPrice());
        Order savedOrder = orderRepository.save(order);

        MatchingEngine engine = matchingEngines.computeIfAbsent(savedOrder.getSymbol().getName(), symbol -> {
            OrderBook book = orderBooks.computeIfAbsent(symbol, s -> new OrderBook());
            return new MatchingEngine(book);
        });

        MatchResult result = engine.processOrder(savedOrder);

        if (!result.getTrades().isEmpty()) {
            tradeRepository.saveAll(result.getTrades());
            logger.info("Executed {} trades.", result.getTrades().size());
        }
        if (!result.getUpdatedOrders().isEmpty()) {
            orderRepository.saveAll(result.getUpdatedOrders());
        }
        return savedOrder;
    }

    // متدهای دیگر مانند cancelOrder, getOrderById و ... را می‌توانید در اینجا پیاده‌سازی کنید
}