package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.DTO.PortfolioItemDto;
import ir.tejarattrd.oms.demo.demo.DTO.PortfolioResponseDto; // FIX: Import changed to the correct DTO
import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Entity.PortfolioItem;
import ir.tejarattrd.oms.demo.demo.Entity.Trade;
import ir.tejarattrd.oms.demo.demo.Repository.CustomerRepository;
import ir.tejarattrd.oms.demo.demo.Repository.PortfolioItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * این کلاس، پیاده‌سازی منطق مدیریت پورتفولیو است.
 */
@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public PortfolioServiceImpl(CustomerRepository customerRepository, PortfolioItemRepository portfolioItemRepository) {
        this.customerRepository = customerRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    @Override
    // FIX: The method signature now returns the correct DTO type.
    public PortfolioResponseDto getPortfolioForCustomer(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("مشتری پیدا نشد: " + username));

        List<PortfolioItem> items = portfolioItemRepository.findByCustomerId(customer.getId());
        List<PortfolioItemDto> itemDtos = items.stream()
                .map(PortfolioItemDto::new) // This line now compiles correctly.
                .collect(Collectors.toList());

        // FIX: A new instance of the correct DTO is returned.
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

        // به‌روزرسانی پورتفولیوی فروشنده
        seller.setBalance(seller.getBalance().add(tradeValue));
        PortfolioItem sellerItem = portfolioItemRepository.findByCustomerIdAndSymbol(seller.getId(), trade.getSymbol())
                .orElseThrow(() -> new IllegalStateException("فروشنده سهام مورد نظر را برای فروش ندارد."));
        sellerItem.setQuantity(sellerItem.getQuantity() - trade.getQuantity());

        // به‌روزرسانی پورتفولیوی خریدار
        buyer.setBalance(buyer.getBalance().subtract(tradeValue));
        PortfolioItem buyerItem = portfolioItemRepository.findByCustomerIdAndSymbol(buyer.getId(), trade.getSymbol())
                .orElseGet(() -> {
                    PortfolioItem newItem = new PortfolioItem();
                    newItem.setCustomerId(buyer.getId());
                    newItem.setSymbol(trade.getSymbol());
                    newItem.setQuantity(0L); // شروع با مقدار صفر
                    return newItem;
                });
        buyerItem.setQuantity(buyerItem.getQuantity() + trade.getQuantity());

        // ذخیره تغییرات
        customerRepository.save(seller);
        customerRepository.save(buyer);
        portfolioItemRepository.save(sellerItem);
        portfolioItemRepository.save(buyerItem);
    }
}

