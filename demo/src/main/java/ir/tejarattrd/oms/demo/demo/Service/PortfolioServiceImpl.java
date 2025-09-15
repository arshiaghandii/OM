package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.DTO.PortfolioItemDto;
import ir.tejarattrd.oms.demo.demo.Entity.Order;
import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;
import ir.tejarattrd.oms.demo.demo.Repository.OrderRepository;
import ir.tejarattrd.oms.demo.demo.Repository.PortfolioItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioItemRepository portfolioItemRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public PortfolioServiceImpl(PortfolioItemRepository portfolioItemRepository, OrderRepository orderRepository) {
        this.portfolioItemRepository = portfolioItemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<PortfolioItemDto> getPortfolioForCustomer(String username) {
        List<PortfolioItem> portfolioItems = portfolioItemRepository.findByCustomer_Username(username);
        if (portfolioItems.isEmpty()) {
            return Collections.emptyList();
        }

        List<Order> allBuyOrders = orderRepository.findByCustomer_UsernameAndOrderType(username, "buy");

        Map<String, List<Order>> ordersBySymbol = allBuyOrders.stream()
                .collect(Collectors.groupingBy(order -> order.getSymbol().getSymbolCode()));

        return portfolioItems.stream()
                .map(item -> convertToDto(item, ordersBySymbol.getOrDefault(item.getSymbol().getSymbolCode(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private PortfolioItemDto convertToDto(PortfolioItem item, List<Order> buyOrders) {
        double totalCost = 0;
        // تغییر از int به Double برای هماهنگی با نوع فیلد Quantity در انتیتی Order
        double totalQuantityBought = 0;

        for (Order order : buyOrders) {
            // **اصلاح اصلی در این بخش انجام شده است**
            // به جای getUnitPrice که وجود ندارد، از getTotalPrice استفاده می‌کنیم
            totalCost += order.getTotalPrice();
            totalQuantityBought += order.getQuantity();
        }

        // محاسبه میانگین قیمت خرید (قیمت سر به سر)
        double averageBuyPrice = (totalQuantityBought > 0) ? totalCost / totalQuantityBought : 0;

        // محاسبه ارزش روز دارایی بر اساس قیمت لحظه‌ای نماد
        double currentValue = item.getQuantity() * item.getSymbol().getUnitPrice();

        // محاسبه قیمت تمام شده دارایی فعلی
        double currentTotalCost = item.getQuantity() * averageBuyPrice;

        // محاسبه سود و زیان
        double profitLoss = currentValue - currentTotalCost;

        // ساخت DTO برای ارسال به فرانت‌اند
        PortfolioItemDto dto = new PortfolioItemDto();
        dto.setSymbolCode(item.getSymbol().getSymbolCode());
        dto.setCompanyName(item.getSymbol().getCompanyName());
        dto.setQuantity(item.getQuantity());
        dto.setCurrentValue(currentValue);
        dto.setProfitLoss(profitLoss);

        return dto;
    }
}