import java.io.Serializable;

public class AdminLoginResponse extends Response implements Serializable {
    final private String status;
    public AdminLoginResponse(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
