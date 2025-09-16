package ir.tejarattrd.oms.demo.demo.Controller;


import ir.tejarattrd.oms.demo.demo.DTO.LoginForm;
import ir.tejarattrd.oms.demo.demo.DTO.RegisterForm;
import ir.tejarattrd.oms.demo.demo.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // STABILITY: Added @Valid to trigger validation on the request body.
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterForm registerForm) {
        authService.register(registerForm);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    // STABILITY: Added @Valid to trigger validation on the request body.
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm) {
        String token = authService.login(loginForm);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return ResponseEntity.ok().headers(headers).body(Map.of("token", token));
    }

}
