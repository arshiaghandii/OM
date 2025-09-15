package ir.tejarattrd.oms.demo.demo.Core;

import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Entity.Trade;
import lombok.Getter;
import java.util.*;

@Getter
public class MatchResult {
    private final List<Trade> trades = new ArrayList<>();
    private final Set<Order> updatedOrders = new HashSet<>();

    public void addTrade(Trade trade) { this.trades.add(trade); }
    public void addUpdatedOrder(Order order) { this.updatedOrders.add(order); }
    public Collection<Order> getUpdatedOrders() { return this.updatedOrders; }
}