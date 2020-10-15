package Server;

import java.io.Serializable;

public class BookingResponse implements Serializable {

    long[] bookingIds;
    String[] seatsAlloted;
    int confirmedSeats;

    public BookingResponse(long[] bookingIds, String[] seatsAlloted, int confirmedSeats) {
        this.bookingIds = bookingIds;
        this.seatsAlloted = seatsAlloted;
        this.confirmedSeats = confirmedSeats;
    }
}
