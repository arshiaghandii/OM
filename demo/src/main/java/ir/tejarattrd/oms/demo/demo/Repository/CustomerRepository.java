package ir.tejarattrd.oms.demo.demo.Repository;

import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer c = new Customer();
            c.setId(rs.getLong("id"));
            c.setFirst_name(rs.getString("first_name"));
            c.setLast_name(rs.getString("last_name"));
            c.setUsername(rs.getString("username"));
            c.setEmail(rs.getString("email"));
            c.setPhone(rs.getString("phone"));
            c.setPassword(rs.getString("password"));
            return c;
        }
    }

    public Optional<Customer> findByUsernameOrEmail(String usernameOrEmail) {
        String sql = "SELECT * FROM customers WHERE username = ? OR email = ?";
        List<Customer> list = jdbcTemplate.query(sql, new CustomerRowMapper(), usernameOrEmail, usernameOrEmail);
        return list.stream().findFirst();
    }

    public List<Customer> findAll() {
        String sql = "SELECT * FROM customers";
        return jdbcTemplate.query(sql, new CustomerRowMapper());
    }

    public Optional<Customer> findById(Long id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        List<Customer> list = jdbcTemplate.query(sql, new CustomerRowMapper(), id);
        return list.stream().findFirst();
    }

    public Optional<Customer> findByEmail(String email) {
        String sql = "SELECT * FROM customers WHERE email = ?";
        List<Customer> list = jdbcTemplate.query(sql, new CustomerRowMapper(), email);
        return list.stream().findFirst();
    }

    public Optional<Customer> findByUsername(String username) {
        String sql = "SELECT * FROM customers WHERE username = ?";
        List<Customer> list = jdbcTemplate.query(sql, new CustomerRowMapper(), username);
        return list.stream().findFirst();
    }

    public Customer save(Customer customer) {
        String sql = "INSERT INTO customers (first_name, last_name, username, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                customer.getFirst_name(),
                customer.getLast_name(),
                customer.getUsername(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getPassword());
        return customer; // برگردوندن خود Customer
    }



    // Update Customer and return the updated entity
    public Customer update(Customer customer) {
        String sql = "UPDATE customers SET firstname = ?, lastname = ?, username = ?, email = ?, phone = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                customer.getFirst_name(),
                customer.getLast_name(),
                customer.getUsername(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getPassword(),
                customer.getId());
        return customer; // برگردوندن خود Customer
    }




    // Delete Customer by id and return the deleted entity (optional)
    public Customer deleteById(Long id) {
        Customer customer = findById(id).orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        String sql = "DELETE FROM customers WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return customer;
    }
}
