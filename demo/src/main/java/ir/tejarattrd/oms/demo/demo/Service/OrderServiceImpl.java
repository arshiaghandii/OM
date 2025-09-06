package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() -> new RuntimeException("سفارشی با شناسه " + id + " پیدا نشد"));
    }

    @Override
    public List<Order> getOrdersByCustomerId(Long customerId) {
        logger.info("Fetching orders for customer id: {}", customerId);
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public Order saveOrder(Order order) {
        // **اصلاح کلیدی: بررسی موجودی از اینجا حذف شد.**
        // وظیفه این متد فقط ذخیره کردن سفارش معتبری است که از کنترلر می‌آید.
        logger.info("Saving new order for customer id: {}", order.getCustomer().getId());
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long id, Order orderDetails) {
        logger.info("Updating order with id: {}", id);
        Order existingOrder = getOrderById(id);
        existingOrder.setCustomer(orderDetails.getCustomer());
        existingOrder.setSymbol(orderDetails.getSymbol());
        existingOrder.setOrderType(orderDetails.getOrderType());
        existingOrder.setQuantity(orderDetails.getQuantity());
        existingOrder.setTotalPrice(orderDetails.getTotalPrice());
        existingOrder.setStatus(orderDetails.getStatus());
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        logger.info("Deleting order with id: {}", id);
        if (!orderRepository.existsById(id)) {
            logger.error("Failed to delete order with id: {}. Not found.", id);
            throw new RuntimeException("سفارشی برای حذف با شناسه " + id + " پیدا نشد");
        }
        orderRepository.deleteById(id);
        logger.info("Order deleted successfully");
    }
}