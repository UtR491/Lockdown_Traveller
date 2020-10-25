import java.io.Serializable;

public class RemoveSeatsResponse extends Response implements Serializable {
    String response;
    RemoveSeatsResponse(String response)
    {
        this.response=response;
    }

    public String getResponse() {
        return response;
    }
}
