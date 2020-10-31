import java.io.Serializable;

public class AdminLoginResponse extends Response implements Serializable {
    final private String status;
    final private String adminID;
    public AdminLoginResponse(String status, String adminID) {
        this.status = status;
        this.adminID = adminID;
    }
    public String getStatus() {
        return status;
    }

    public String getAdminID() {
        return adminID;
    }
}
