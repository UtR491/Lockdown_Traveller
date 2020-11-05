import java.io.Serializable;

/**
 * Response given by the server after the login request is processed.
 */
public class LoginResponse extends Response implements Serializable {

    final private String userId, name, username, email, phone;

    /**
     * Constructor to initialize the login response.
     * @param userId Identifier of the logged in user.
     * @param name Name of the user.
     * @param username Username of the user.
     * @param email Email of the user.
     * @param phone Phone number of the user.
     */
    public LoginResponse(String userId, String name, String username, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Getter for the user id.
     * @return User id.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Getter for the name.
     * @return Name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the username.
     * @return Username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the user email.
     * @return Email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for the user phone number.
     * @return Phone number.
     */
    public String getPhone() {
        return phone;
    }
}
