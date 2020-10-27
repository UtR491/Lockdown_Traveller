import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationHandler extends Handler {
    Connection connection;
    CancelBookingRequest cb;
    ObjectOutputStream oos;
    NotificationHandler (Connection connection,CancelBookingRequest cb,ObjectOutputStream oos)
    {
        this.connection=connection;
        this.cb=cb;
        this.oos=oos;
    }
    BookingRequest bookingRequest;
    String userId = bookingRequest.getUserId();
    PreparedStatement sendNotification;

    {
        try {
            sendNotification = Server.getConnection().prepareStatement("select * from Notification where UserID =?");
            sendNotification.setString(1,userId );
            sendNotification.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
//    add notification table in the database;


}
