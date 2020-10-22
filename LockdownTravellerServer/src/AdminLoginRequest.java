import java.io.Serializable;

public class AdminLoginRequest extends Request implements Serializable {
    final private String username;
    final private String password;
    public AdminLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
