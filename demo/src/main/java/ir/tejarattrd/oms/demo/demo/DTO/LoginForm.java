package ir.tejarattrd.oms.demo.demo.DTO;

public class LoginForm {
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public long getPassword() {
        return password;
    }

    public void setPassword(long password) {
        this.password = password;
    }

    private String usernameOrEmail;
    private long password;

}
