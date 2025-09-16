package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Core.MatchResult;
import ir.tejarattrd.oms.demo.demo.Core.MatchingEngine;
import ir.tejarattrd.oms.demo.demo.Core.OrderBook;
import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Repository.OrderRepository;
import ir.tejarattrd.oms.demo.demo.Repository.TradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderProcessingService {

    private final Map<String, MatchingEngine> matchingEngines = new ConcurrentHashMap<>();
    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;
    private final PortfolioService portfolioService;

    public OrderProcessingService(OrderRepository orderRepository, TradeRepository tradeRepository, PortfolioService portfolioService) {
        this.orderRepository = orderRepository;
        this.tradeRepository = tradeRepository;
        this.portfolioService = portfolioService;
    }

    // FIX: The missing processOrder method has been added.
    // This method takes an order, finds the correct matching engine for the order's symbol,
    // and then processes it. This resolves the compilation error in StartupOrderProcessor.
    @Transactional
    public void processOrder(Order order) {
        MatchingEngine engine = matchingEngines.computeIfAbsent(order.getSymbol(), s -> new MatchingEngine(new OrderBook(s)));
        MatchResult result = engine.processOrder(order);

        // Save the results of the matching process
        if (!result.getTrades().isEmpty()) {
            tradeRepository.saveAll(result.getTrades());
        }
        if (!result.getUpdatedOrders().isEmpty()) {
            orderRepository.saveAll(result.getUpdatedOrders());
            result.getTrades().forEach(portfolioService::updatePortfolioAfterTrade);
        }
    }
}

