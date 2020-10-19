
import java.io.Serializable;

public class CancelBookingResponse extends Response implements Serializable {
    String response;
    CancelBookingResponse(String response)
    {
        this.response=response;
    }

    public String getResponse() {
        return response;
    }
}
