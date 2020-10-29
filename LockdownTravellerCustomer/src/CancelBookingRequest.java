import java.io.*;

public class CancelBookingRequest extends Request implements Serializable {
    final private String PNR, userId;
    CancelBookingRequest(String PNR, String userID) {
        this.PNR=PNR;
        this.userId = userID;
    }
    public String getPNR() {
        return PNR;
    }

    public String getUserId() {
        return userId;
    }
}
