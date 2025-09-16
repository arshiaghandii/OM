package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.DTO.PortfolioItemDto;
import ir.tejarattrd.oms.demo.demo.DTO.PortfolioResponseDto;
import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;
import ir.tejarattrd.oms.demo.demo.Entity.Trade;
import ir.tejarattrd.oms.demo.demo.Repository.CustomerRepository;
import ir.tejarattrd.oms.demo.demo.Repository.PortfolioItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public PortfolioServiceImpl(CustomerRepository customerRepository, PortfolioItemRepository portfolioItemRepository) {
        this.customerRepository = customerRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    @Override
    public PortfolioResponseDto getPortfolioForCustomer(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("مشتری پیدا نشد: " + username));

        List<PortfolioItem> items = portfolioItemRepository.findByCustomerId(customer.getId());
        List<PortfolioItemDto> itemDtos = items.stream()
                .map(PortfolioItemDto::new)
                .collect(Collectors.toList());

        return new PortfolioResponseDto(customer.getBalance(), itemDtos);
    }

    @Override
    @Transactional
    public void updatePortfolioAfterTrade(Trade trade) {
        Customer buyer = customerRepository.findById(trade.getBuyerId())
                .orElseThrow(() -> new RuntimeException("خریدار پیدا نشد"));
        Customer seller = customerRepository.findById(trade.getSellerId())
                .orElseThrow(() -> new RuntimeException("فروشنده پیدا نشد"));

        BigDecimal tradeValue = trade.getPrice().multiply(new BigDecimal(trade.getQuantity()));
        long tradeQuantity = trade.getQuantity();

        // --- به‌روزرسانی پورتفولیوی فروشنده ---
        seller.setBalance(seller.getBalance().add(tradeValue));
        PortfolioItem sellerItem = portfolioItemRepository.findByCustomerIdAndSymbolId(seller.getId(), trade.getSellerId())
                .orElseThrow(() -> new IllegalStateException("فروشنده سهام مورد نظر را برای فروش ندارد."));

        // اگر فروشنده تمام سهامش را فروخت، آیتم را حذف می‌کنیم
        if (sellerItem.getQuantity() - tradeQuantity == 0) {
            portfolioItemRepository.delete(sellerItem);
        } else {
            sellerItem.setQuantity(sellerItem.getQuantity() - tradeQuantity);
            portfolioItemRepository.save(sellerItem);
        }
        customerRepository.save(seller);


        // --- به‌روزرسانی پورتفولیوی خریدار ---
        buyer.setBalance(buyer.getBalance().subtract(tradeValue));
        PortfolioItem buyerItem = portfolioItemRepository.findByCustomerIdAndSymbolId(buyer.getId(), trade.getBuyerId())
                .orElseGet(() -> {
                    PortfolioItem newItem = new PortfolioItem();
                    // --- FIX: از متد جدید و صحیح استفاده شد ---
                    newItem.setCustomer(buyer);
                    // --- FIX END ---
                    newItem.setSymbol(trade.getCompanyName());
                    newItem.setQuantity(0L);
                    newItem.setAveragePrice(BigDecimal.ZERO); // مقدار اولیه برای محاسبه
                    return newItem;
                });

        // === منطق کلیدی محاسبه قیمت میانگین جدید ===
        long oldQuantity = buyerItem.getQuantity();
        BigDecimal oldAveragePrice = buyerItem.getAveragePrice();

        BigDecimal oldTotalValue = oldAveragePrice.multiply(BigDecimal.valueOf(oldQuantity));
        BigDecimal tradeTotalValue = trade.getPrice().multiply(BigDecimal.valueOf(tradeQuantity));
        BigDecimal newTotalValue = oldTotalValue.add(tradeTotalValue);

        long newQuantity = oldQuantity + tradeQuantity;

        // میانگین جدید = ارزش کل جدید / تعداد کل جدید
        BigDecimal newAveragePrice = newTotalValue.divide(BigDecimal.valueOf(newQuantity), 8, RoundingMode.HALF_UP);

        buyerItem.setQuantity(newQuantity);
        buyerItem.setAveragePrice(newAveragePrice); // ذخیره میانگین جدید

        // ذخیره تغییرات خریدار
        customerRepository.save(buyer);
        portfolioItemRepository.save(buyerItem);
    }
}