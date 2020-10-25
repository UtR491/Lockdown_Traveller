import java.io.Serializable;

public class RerouteResponse extends Response implements Serializable {
    private  String response;
    public RerouteResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
