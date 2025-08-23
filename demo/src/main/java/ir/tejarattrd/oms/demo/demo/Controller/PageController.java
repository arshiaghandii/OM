package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.DTO.LoginForm;
import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {

    private final CustomerService customerService;

    public PageController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/")
    public String home() {
        return "home/home";
    }

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
            // کاربر را به صفحه ورود می‌فرستیم تا با اطلاعات جدیدش وارد شود
            redirectAttributes.addFlashAttribute("successMessage", "ثبت‌نام با موفقیت انجام شد. لطفاً وارد شوید.");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            model.addAttribute("registrationError", e.getMessage());
            return "home/register/register";
        }
    }

    // متد logout کاملاً حذف می‌شود. Spring Security آن را مدیریت می‌کند.
}