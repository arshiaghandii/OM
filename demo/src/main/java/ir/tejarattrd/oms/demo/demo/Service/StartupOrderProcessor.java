package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
// NO-LOMBOK: Removed @Slf4j
public class StartupOrderProcessor {

    // NO-LOMBOK: Added manual logger
    private static final Logger log = LoggerFactory.getLogger(StartupOrderProcessor.class);

    private final OrderRepository orderRepository;
    private final OrderProcessingService orderProcessingService;

    public StartupOrderProcessor(OrderRepository orderRepository, OrderProcessingService orderProcessingService) {
        this.orderRepository = orderRepository;
        this.orderProcessingService = orderProcessingService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void processOpenOrdersOnStartup() {
        log.info("Searching for open orders to process on application startup...");
        List<Order> openOrders = orderRepository.findUnfilledOrders();

        if (openOrders.isEmpty()) {
            log.info("No open orders found to process.");
            return;
        }

        log.info("Found {} open orders. Submitting to processing service.", openOrders.size());
        openOrders.forEach(orderProcessingService::processOrder);
        log.info("Finished submitting all open orders from startup.");
    }
}

