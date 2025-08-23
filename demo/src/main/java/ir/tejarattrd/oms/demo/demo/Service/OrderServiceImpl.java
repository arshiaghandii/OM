package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ir.tejarattrd.oms.demo.demo.Service.OrderService;
import java.util.List;
/*-------------------------------------------------------------------------------- */

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        logger.info("Fetching order with id: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found with id: {}", id);
                    return new RuntimeException("Order not found with id: " + id);
                });
    }

    @Override
    public List<Order> getOrdersByCustomerId(Long customerId) {
        logger.info("Fetching orders for customer id: {}", customerId);
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public Order saveOrder(Order order) {
        logger.info("Saving new order for customer id: {}", order.getCustomer().getId());
        if (order.getOrderDate() == null) {
            order.setOrderDate(java.time.LocalDateTime.now());
        }

        int result = orderRepository.save(order);
        if (result > 0) {
            logger.info("Order saved successfully");
            return order;
        } else {
            logger.error("Failed to save order");
            throw new RuntimeException("Failed to save order");
        }
    }
    @Override
    public Order updateOrder(Long id, Order order) {
        logger.info("Updating order with id: {}", id);
        Order existing = getOrderById(id); // از متد تعریف شده استفاده می‌کنیم

        // آپدیت فیلدهای قابل تغییر
        existing.setCustomer(order.getCustomer());
        existing.setSymbol(order.getSymbol());
        existing.setOrderType(order.getOrderType());
        existing.setQuantity(order.getQuantity());
        existing.setTotalPrice(order.getTotalPrice());
        existing.setOrderDate(order.getOrderDate() != null ? order.getOrderDate() : existing.getOrderDate());
        existing.setStatus(order.getStatus());

        int result = orderRepository.update(existing);
        if (result > 0) {
            logger.info("Order updated successfully");
            return existing;
        } else {
            logger.error("Failed to update order with id: {}", id);
            throw new RuntimeException("Failed to update order with id: " + id);
        }
    }

    @Override
    public void deleteOrder(Long id) {
        logger.info("Deleting order with id: {}", id);
        int result = orderRepository.deleteById(id);
        if (result == 0) {
            logger.error("Failed to delete order with id: {}", id);
            throw new RuntimeException("Failed to delete order with id: " + id);
        }
        logger.info("Order deleted successfully");
    }
}

