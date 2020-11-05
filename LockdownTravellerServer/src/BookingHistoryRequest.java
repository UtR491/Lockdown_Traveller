import java.io.Serializable;

/**
 * Request class for booking history enquiry.
 */
public class BookingHistoryRequest extends  Request implements Serializable {

    final private String userid;

    /**
     * Constructor for the booking history request.
     * @param userid To identify the user whose bookings have to be looked up.
     */
    BookingHistoryRequest(String userid){
        this.userid=userid;
    }

    /**
     * Getter for the user id.
     * @return User id.
     */
    public String getUserid(){
        return userid;
    }
}
