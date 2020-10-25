import java.io.Serializable;

public class RemoveTrainsResponse extends Response implements Serializable {
    final private String response;
    RemoveTrainsResponse(String response)
    {
        this.response=response;
    }

    public String getResponse() {
        return response;
    }
}
