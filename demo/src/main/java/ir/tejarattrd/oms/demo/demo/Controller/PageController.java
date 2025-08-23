package ir.tejarattrd.oms.demo.demo.Controller;

import ir.tejarattrd.oms.demo.demo.Entity.Customer;
import ir.tejarattrd.oms.demo.demo.Service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    private final CustomerService customerService;

    public PageController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "home/home"; // Thymeleaf home.html
    }




    @GetMapping("/register")
    public String registerPage() {
        return "home/register/register"; // Thymeleaf register.html
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Customer customer, HttpSession session) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        // بعد از ثبت‌نام، کاربر اتوماتیک لاگین می‌شود
        session.setAttribute("currentUser", savedCustomer);
        return "redirect:/"; // انتقال به صفحه اصلی
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
