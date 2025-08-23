//package ir.tejarattrd.oms.demo.demo.Controller;
//
//import ir.tejarattrd.oms.demo.demo.DTO.LoginForm;
//import ir.tejarattrd.oms.demo.demo.Service.AuthService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin(origins = "*")
//public class AuthRestController {
//
//    private final AuthService authService;
//
//    public AuthRestController(AuthService authService) {
//        this.authService = authService;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
//
//    }
//
//    // کلاس داخلی برای پاسخ JWT
//    public static class JwtResponse {
//        private String token;
//
//        public JwtResponse(String token) { this.token = token; }
//        public String getToken() { return token; }
//        public void setToken(String token) { this.token = token; }
//    }
//}
