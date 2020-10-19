import java.io.*;

public class CancelBookingRequest extends Request implements Serializable {
    private String PNR;
    CancelBookingRequest(String PNR)
    {
        this.PNR=PNR;
    }
    public String getPNR() {
        return PNR;
    }
}
