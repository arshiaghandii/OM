package ir.tejarattrd.oms.demo.demo.Repository;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
/*-------------------------------------------------------------------------------- */

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setOrderId(rs.getLong("order_id"));

            Customer customer = new Customer();
            customer.setId(rs.getLong("customer_id"));
            order.setCustomer(customer);

            Symbol symbol = new Symbol();
            symbol.setSymbolId(rs.getLong("symbol_id"));
            order.setSymbol(symbol);

            order.setOrderType(rs.getString("order_type"));
            order.setQuantity((double) rs.getInt("quantity"));
            order.setTotalPrice(rs.getDouble("total_price"));
            order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
            order.setStatus(rs.getString("status"));

            return order;
        }
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        List<Order> list = jdbcTemplate.query(sql, new OrderRowMapper(), id);
        return list.stream().findFirst();
    }

    public List<Order> findByCustomerId(Long customerId) {
        String sql = "SELECT * FROM orders WHERE customer_id = ?";
        return jdbcTemplate.query(sql, new OrderRowMapper(), customerId);
    }

    public int save(Order order) {
        String sql = "INSERT INTO orders (customer_id, symbol_id, order_type, quantity, total_price, order_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                order.getCustomer().getId(),
                order.getSymbol().getSymbolId(),
                order.getOrderType(),
                order.getQuantity(),
                order.getTotalPrice(),
                java.sql.Timestamp.valueOf(order.getOrderDate()),
                order.getStatus());
    }

    public int update(Order order) {
        String sql = "UPDATE orders SET customer_id = ?, symbol_id = ?, order_type = ?, quantity = ?, total_price = ?, order_date = ?, status = ? WHERE order_id = ?";
        return jdbcTemplate.update(sql,
                order.getCustomer().getId(),
                order.getSymbol().getSymbolId(),
                order.getOrderType(),
                order.getQuantity(),
                order.getTotalPrice(),
                java.sql.Timestamp.valueOf(order.getOrderDate()),
                order.getStatus(),
                order.getOrderId());
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
