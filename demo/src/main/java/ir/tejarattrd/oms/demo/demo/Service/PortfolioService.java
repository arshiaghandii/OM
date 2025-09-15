package ir.tejarattrd.oms.demo.demo.Service;

import ir.tejarattrd.oms.demo.demo.DTO.PortfolioItemDto;
import java.util.List;

public interface PortfolioService {
    List<PortfolioItemDto> getPortfolioForCustomer(String username);
}