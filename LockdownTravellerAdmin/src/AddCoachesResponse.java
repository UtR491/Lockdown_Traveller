import java.io.Serializable;

public class AddCoachesResponse extends Response implements Serializable {
    String response;
    AddCoachesResponse(String response)
    {
        this.response=response;
    }

    public String getResponse() {
        return response;
    }
}
