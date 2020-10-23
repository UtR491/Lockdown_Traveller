import java.io.Serializable;

public class CancelTrainsResponse extends Response implements Serializable {
    String response;
    CancelTrainsResponse(String response)
    {
        this.response=response;
    }

    public String getResponse() {
        return response;
    }
}
