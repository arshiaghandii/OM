package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.DTO.SymbolDto;
import ir.tejarattrd.oms.demo.demo.Repository.SymbolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class PageController {

    private final SymbolRepository symbolRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "home/login/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "home/register/register";
    }

    @GetMapping
    public String homePage(Model model) {
        List<SymbolDto> symbols = symbolRepository.findAll()
                .stream()
                .map(SymbolDto::fromEntity) // <-- این خط اصلاح شد
                .collect(Collectors.toList());
        model.addAttribute("symbols", symbols);
        return "home/home";
    }

    @GetMapping("/order")
    public String orderPage(Model model) {
        List<SymbolDto> symbols = symbolRepository.findAll()
                .stream()
                .map(SymbolDto::fromEntity) // <-- این خط اصلاح شد
                .collect(Collectors.toList());
        model.addAttribute("symbols", symbols);
        return "home/orderP/Order";
    }

    @GetMapping("/portfolio")
    public String portfolioPage() {
        return "home/portfolio/portfolio";
    }
}