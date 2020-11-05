import java.io.Serializable;

/**
 * Server's response to the booking request.
 */
public class BookingResponse extends Response implements Serializable {

    private final String[] bookingIds;
    private final String[] seatsAlloted;
    private final int confirmedSeats;
    private final String pnr;
    private final int totalCost;

    /**
     * Constructor to initialize booking response variables.
     * @param bookingIds Unique identifier for a booking id.
     * @param seatsAlloted Seat alloted to the corresponding booking id.
     * @param confirmedSeats Number of seats confirmed.
     * @param pnr Identifier for the entire booking.
     * @param totalCost Final cost after discount deductions.
     */
    public BookingResponse(String[] bookingIds, String[] seatsAlloted, int confirmedSeats, String pnr, int totalCost) {
        this.bookingIds = bookingIds;
        this.seatsAlloted = seatsAlloted;
        this.confirmedSeats = confirmedSeats;
        this.pnr = pnr;
        this.totalCost = totalCost;
    }

    /**
     * Getter for booking id.
     * @return Booking id array.
     */
    public String[] getBookingIds() {
        return bookingIds;
    }

    /**
     * Getter for the alloted seats.
     * @return Alloted seats array.
     */
    public String[] getSeatsAlloted() {
        return seatsAlloted;
    }

    /**
     * Getter for number of seats confirmed.
     * @return Number of seats confirmed
     */
    public int getConfirmedSeats() {
        return confirmedSeats;
    }

    /**
     * Getter for PNR.
     * @return PNR.
     */
    public String getPnr() {
        return pnr;
    }

    /**
     * Getter for total cost.
     * @return Total cost.
     */
    public int getTotalCost() {
        return totalCost;
    }
}
