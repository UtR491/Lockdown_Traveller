import java.io.Serializable;

public class AddTrainsResponse extends Response implements Serializable {
    String response;
    AddTrainsResponse(String response)
    {
        this.response=response;
    }

    public String getResponse() {
        return response;
    }
}
