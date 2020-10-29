
import java.io.Serializable;

public class BookingResponse extends Response implements Serializable {

    private final String[] bookingIds;
    private final String[] seatsAlloted;
    private final int confirmedSeats;
    private final String pnr;

    public String[] getBookingIds() {
        return bookingIds;
    }

    public String[] getSeatsAlloted() {
        return seatsAlloted;
    }

    public int getConfirmedSeats() {
        return confirmedSeats;
    }

    public String getPnr() {
        return pnr;
    }

    public BookingResponse(String[] bookingIds, String[] seatsAlloted, int confirmedSeats, String pnr) {
        this.bookingIds = bookingIds;
        this.seatsAlloted = seatsAlloted;
        this.confirmedSeats = confirmedSeats;
        this.pnr = pnr;
    }
}
