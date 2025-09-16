package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.DTO.PortfolioDto;
import ir.tejarattrd.oms.demo.demo.Repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public List<PortfolioDto> getPortfolioByCustomerId(Long customerId) {
        return portfolioRepository.findAll().stream() // فعلا همه را می‌گیریم، بعدا بر اساس customerId فیلتر می‌کنیم
                .filter(item -> item.getCustomer().getId().equals(customerId))
                .map(PortfolioDto::new) // تبدیل هر آیتم به DTO
                .collect(Collectors.toList());
    }
}