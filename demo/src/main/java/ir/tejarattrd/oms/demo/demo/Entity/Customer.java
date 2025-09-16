package ir.tejarattrd.oms.demo.demo.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal; // **ایمپورت جدید**
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer {

    // ... (فیلدهای دیگر بدون تغییر)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "نام نمی‌تواند خالی باشد")
    @Column(nullable = false)
    private String first_name;

    @NotBlank(message = "نام خانوادگی نمی‌تواند خالی باشد")
    @Column(nullable = false)
    private String last_name;

    @NotBlank(message = "نام کاربری نمی‌تواند خالی باشد")
    @Size(min = 4, message = "نام کاربری باید حداقل ۴ کاراکتر باشد")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "ایمیل نمی‌تواند خالی باشد")
    @Email(message = "فرمت ایمیل صحیح نیست")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "رمز عبور نمی‌تواند خالی باشد")
    @Size(min = 6, message = "رمز عبور باید حداقل ۶ کاراکتر باشد")
    private String password;

    @NotBlank(message = "شماره همراه نمی‌تواند خالی باشد")
    @Size(min = 11, max = 11, message = "شماره همراه باید ۱۱ کاراکتر باشد")
    private String phone;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // **تغییر کلیدی: نوع داده به BigDecimal تغییر کرد**
    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PortfolioItem> portfolio;

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Set<Order> getOrders() { return orders; }
    public void setOrders(Set<Order> orders) { this.orders = orders; }
    public Set<PortfolioItem> getPortfolio() { return portfolio; }
    public void setPortfolio(Set<PortfolioItem> portfolio) { this.portfolio = portfolio; }

    // **Getter & Setter برای فیلد جدید**
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}