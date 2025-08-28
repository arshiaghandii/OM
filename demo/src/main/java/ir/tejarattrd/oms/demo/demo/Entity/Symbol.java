    package ir.tejarattrd.oms.demo.demo.Entity;

    import jakarta.persistence.*;
    import java.time.LocalDateTime;
    import java.util.Set;
    /*-------------------------------------------------------------------------------- */

    @Entity
    @Table(name = "symbols")
    public class Symbol {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long symbolId;

        @Column(name = "symbol_code", unique = true, nullable = false)
        private String symbolCode;

        @Column(name = "company_name")
        private String companyName;

        @Column(name = "market")
        private String market;

        @Column(name = "unit_price", precision = 18)
        private Double unitPrice;

        @Column(name = "trading_volume")
        private Long tradingVolume;

        @Column(name = "last_price_update")
        private LocalDateTime lastPriceUpdate;

        @OneToMany(mappedBy = "symbol", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Set<Order> orders;


        /*------------------------------------------------------------------------------------------------------------------------------*/



        public Long getTradingVolume() {return tradingVolume;}

        public void setTradingVolume(Long tradingVolume) {this.tradingVolume = tradingVolume;}


        // Getters and Setters
        public Long getSymbolId() { return symbolId; }
        public void setSymbolId(Long symbolId) { this.symbolId = symbolId; }

        public String getSymbolCode() { return symbolCode; }
        public void setSymbolCode(String symbolCode) { this.symbolCode = symbolCode; }

        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }

        public String getMarket() { return market; }
        public void setMarket(String market) { this.market = market; }

        public Double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

        public LocalDateTime getLastPriceUpdate() { return lastPriceUpdate; }
        public void setLastPriceUpdate(LocalDateTime lastPriceUpdate) { this.lastPriceUpdate = lastPriceUpdate; }

        public Set<Order> getOrders() { return orders; }
        public void setOrders(Set<Order> orders) { this.orders = orders; }
    }
