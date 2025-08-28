package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.DTO.LoginForm;
import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Entity.Symbol; // اضافه کردن ایمپورت
import ir.tejarattrd.oms.demo.demo.Service.CustomerService;
import ir.tejarattrd.oms.demo.demo.Service.SymbolService; // اضافه کردن ایمپورت
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List; // اضافه کردن ایمپورت

@Controller
public class PageController {

    private final CustomerService customerService;
    private final SymbolService symbolService; // **۱. سرویس نمادها به اینجا اضافه شد**

    // **۲. سازنده کلاس برای تزریق هر دو سرویس اصلاح شد**
    public PageController(CustomerService customerService, SymbolService symbolService) {
        this.customerService = customerService;
        this.symbolService = symbolService;
    }

    // ... متدهای دیگر بدون تغییر ...
    @GetMapping("/")
    public String home() { return "home/home"; }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "home/login/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "home/register/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("customer") Customer customer,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "home/register/register";
        }
        try {
            customerService.saveCustomer(customer);
            redirectAttributes.addFlashAttribute("successMessage", "ثبت‌نام با موفقیت انجام شد. لطفاً وارد شوید.");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            model.addAttribute("registrationError", e.getMessage());
            return "home/register/register";
        }
    }

    @GetMapping("/order")
    public String orderPage(Model model){
        // **۳. متد با استفاده از نمونه تزریق شده اصلاح شد**
        List<Symbol> symbols = symbolService.getAllSymbols();
        model.addAttribute("symbols", symbols);
        return "home/orderP/Order";
    }
}