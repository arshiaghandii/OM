package ir.tejarattrd.oms.demo.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Getter
@Setter
@NoArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol; // نام نماد برای دسترسی سریع
    private BigDecimal price;
    private long quantity;
    private Long buyOrderId;
    private Long sellOrderId;
    private LocalDateTime executedAt;

    public Trade(String symbol, BigDecimal price, long quantity, Long buyOrderId, Long sellOrderId) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.executedAt = LocalDateTime.now();
    }
}