package Server;

import java.io.Serializable;

public class BookingResponse extends Response implements Serializable {

    private final long[] bookingIds;
    private final String[] seatsAlloted;
    private final int confirmedSeats;

    public BookingResponse(long[] bookingIds, String[] seatsAlloted, int confirmedSeats) {
        this.bookingIds = bookingIds;
        this.seatsAlloted = seatsAlloted;
        this.confirmedSeats = confirmedSeats;
    }
}