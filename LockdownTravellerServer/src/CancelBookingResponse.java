import java.io.Serializable;

public class CancelBookingResponse extends Response implements Serializable {

    final private String response;

    /**
     * Constructor to initialize the server's response to the server.
     * @param response Wether cancellation was successful or not.
     */
    CancelBookingResponse(String response)
    {
        this.response=response;
    }

    /**
     * Getter for response.
     * @return Response.
     */
    public String getResponse() {
        return response;
    }
}
