import java.io.Serializable;

/**
 * Response given by the server after AdminLoginRequest is processed.
 */
public class AdminLoginResponse extends Response implements Serializable {

    final private String status;
    final private String adminID;

    /**
     * Constructor for admin login response, initializes the status and admin id as per the response given by the server.
     * @param status Whether the login attempt was successful or not (incorrect credentials or something else).
     * @param adminID Admin ID in case the login attempt was successful.
     */
    public AdminLoginResponse(String status, String adminID) {
        this.status = status;
        this.adminID = adminID;
    }

    /**
     * Getter for the response status.
     * @return Login status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Getter for the Admin ID.
     * @return Admin ID in case the login was successful.
     */
    public String getAdminID() {
        return adminID;
    }
}
