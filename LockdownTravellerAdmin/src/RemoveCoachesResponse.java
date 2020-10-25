import java.io.Serializable;

public class RemoveCoachesResponse extends Response implements Serializable {
    String response;
    RemoveCoachesResponse(String response)
    {
        this.response=response;
    }

    public String getResponse() {
        return response;
    }
}
