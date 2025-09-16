package ir.tejarattrd.oms.demo.demo.DTO;

public record LoginForm(
        String usernameOrEmail,
        String password
) {}