import java.io.Serializable;

/**
 * Request class for user login feature.
 */
public class LoginRequest extends Request implements Serializable {

    final private String username;
    final private String password;

    /**
     * Initialize the request object with entered information.
     * @param username Entered username.
     * @param password Hash of the entered password.
     */
    public LoginRequest(String username, String password){
        this.password=password;
        this.username=username;
    }

    /**
     * Getter for username.
     * @return Username.
     */
    public String getUsername (){
        return this.username;
    }

    /**
     * Getter for password hash.
     * @return Password hash.
     */
    public String getPassword (){
        return this.password;
    }

}
