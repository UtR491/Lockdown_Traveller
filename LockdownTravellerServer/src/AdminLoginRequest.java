import java.io.Serializable;

/**
 * Request class for Admin Login.
 */
public class AdminLoginRequest extends Request implements Serializable {

    final private String username;
    final private String password;

    /**
     * Constructor for the AdminLoginRequest, initializes the username and password with the values entered by the user.
     * @param username Username entered by the user.
     * @param password Password used for autherntication.
     */
    public AdminLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Getter for username.
     * @return Username entered by the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for password.
     * @return Password entered by the user.
     */
    public String getPassword() {
        return password;
    }
}
