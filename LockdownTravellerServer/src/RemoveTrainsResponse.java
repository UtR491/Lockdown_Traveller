import java.io.Serializable;

public class RemoveTrainsResponse extends Response implements Serializable {
    private int status;
    private String response;
    RemoveTrainsResponse(int status)
    {
        this.status=status;
    }
    RemoveTrainsResponse(String response)
    {
        this.response=response;
    }

    public String getResponse() {
        return response;
    }

    public int getStatus() {
        return status;
    }
}
