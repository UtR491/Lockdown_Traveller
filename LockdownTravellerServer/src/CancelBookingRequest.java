import java.io.*;

/**
 * Request class for cancel booking feature.
 */
public class CancelBookingRequest extends Request implements Serializable {

    final private String PNR, userId;

    /**
     * Constructor to initialize the request object.
     * @param PNR It is the booking identifier, so it only makes sense to use it for cancellation.
     * @param userID User id to make sure someone is not try to cancel someone else's tickets by hit and trial.
     */
    CancelBookingRequest(String PNR, String userID) {
        this.PNR=PNR;
        this.userId = userID;
    }

    /**
     * Getter for pnr.
     * @return PNR.
     */
    public String getPNR() {
        return PNR;
    }

    /**
     * Getter for user id.
     * @return
     */
    public String getUserId() {
        return userId;
    }
}
