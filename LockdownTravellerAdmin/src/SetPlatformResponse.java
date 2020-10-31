import java.io.Serializable;

public class SetPlatformResponse extends Response implements Serializable {
    String response;

    public SetPlatformResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
