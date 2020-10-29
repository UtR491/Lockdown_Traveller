import java.io.Serializable;

public class RegisterResponse extends Response implements Serializable {
    final private String response;
    public RegisterResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
