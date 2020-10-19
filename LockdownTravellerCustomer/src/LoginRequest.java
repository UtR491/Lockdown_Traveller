import java.io.Serializable;

public class LoginRequest extends Request implements Serializable {
    private String username;
    private String password;

    public LoginRequest(String username, String password){
        this.password=password;
        this.username=username;

    }
    public String getUsername (){
        return this.username;
    }
    public String getPassword (){
        return this.password;
    }

}
