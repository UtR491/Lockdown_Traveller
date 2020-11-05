import java.io.Serializable;

public class RegisterResponse extends Response implements Serializable {

    final private String response;

    /**
     * Response to the register request. We don't really need to send other information because it is still on the
     * register screen and the information is already there.
     * @param response Whether the register was successful or not.
     */
    public RegisterResponse(String response) {
        this.response = response;
    }

    /**
     * Getter for the response of the server.
     * @return Response.
     */
    public String getResponse() {
        return response;
    }
}
