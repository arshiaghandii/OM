package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.DTO.SymbolDto;
import ir.tejarattrd.oms.demo.demo.Repository.SymbolRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PageController {

    private final SymbolRepository symbolRepository;

    // --- FIX: سازنده به صورت دستی اضافه شد ---
    public PageController(SymbolRepository symbolRepository) {
        this.symbolRepository = symbolRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "home/login/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "home/register/register";
    }

    @GetMapping({"/", "/home"})
    public String homePage(Model model) {
        List<SymbolDto> symbols = symbolRepository.findAll()
                .stream()
                .map(SymbolDto::fromEntity)
                .collect(Collectors.toList());
        model.addAttribute("symbols", symbols);
        return "home/home";
    }

    @GetMapping("/home/order")
    public String orderPage(Model model) {
        List<SymbolDto> symbols = symbolRepository.findAll()
                .stream()
                .map(SymbolDto::fromEntity)
                .collect(Collectors.toList());
        model.addAttribute("symbols", symbols);
        return "home/orderP/Order";
    }

    @GetMapping("/home/portfolio")
    public String portfolioPage() {
        return "home/portfolio/portfolio";
    }
}