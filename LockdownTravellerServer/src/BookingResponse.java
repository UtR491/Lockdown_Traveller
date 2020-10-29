
import java.io.Serializable;

public class BookingResponse extends Response implements Serializable {

    private final String[] bookingIds;
    private final String[] seatsAlloted;
    private final int confirmedSeats;
    private final String pnr;
    private final int totalCost;

    public BookingResponse(String[] bookingIds, String[] seatsAlloted, int confirmedSeats, String pnr, int totalCost) {
        this.bookingIds = bookingIds;
        this.seatsAlloted = seatsAlloted;
        this.confirmedSeats = confirmedSeats;
        this.pnr = pnr;
        this.totalCost = totalCost;
    }

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

    public int getTotalCost() {
        return totalCost;
    }
}
