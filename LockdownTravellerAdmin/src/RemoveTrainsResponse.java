import java.io.Serializable;

public class RemoveTrainsResponse extends Response implements Serializable {
    private String status;
    RemoveTrainsResponse(String status)
    {
        this.status=status;
    }

    public String getStatus() {
        return status;
    }
}
