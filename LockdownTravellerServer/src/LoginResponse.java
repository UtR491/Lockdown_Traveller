
import java.io.Serializable;

public class LoginResponse extends Response implements Serializable {
    private final String userId;

    public LoginResponse(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
