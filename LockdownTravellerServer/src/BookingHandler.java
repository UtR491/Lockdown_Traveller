import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BookingHandler {
    private final Connection connection;
    private final BookingRequest bookingRequest;
    private final ObjectOutputStream oos;
    private String coach = null;
    public BookingHandler(Connection connection, BookingRequest bookingRequest, ObjectOutputStream oos) {
        this.connection = connection;
        this.bookingRequest = bookingRequest;
        this.oos = oos;
    }

    public void sendQuery() {
        String query1 = "select Station from Route_Info where Train_ID = ? and Station_No between" +
                "(select Station_No from Route_Info where Train_ID = ? and Station = ?) and" +
                "(select Station_No from Route_Info where Train_ID = ? and Station = ?);";
    }

    private BookingResponse bookSeats(String query1) {
    }
}