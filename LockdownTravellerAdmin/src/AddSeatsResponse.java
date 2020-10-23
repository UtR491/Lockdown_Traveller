import java.io.Serializable;

public class AddSeatsResponse extends Response implements Serializable
{
    String response;
    AddSeatsResponse(String response)
    {
        this.response=response;
    }

    public String getResponse() {
        return response;
    }
}
