import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notification implements Serializable {
    private String message;
    private String from;
    private String to;
    BookingRequest bookingRequest;


    PreparedStatement notification;
    Date todayDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String strDate = dateFormat.format(todayDate);
    String userid= bookingRequest.getUserId();

    public String notifymessage() {
        try {
            notification = Server.getConnection().prepareStatement("Select Message from notification where User_ID=? and Date=?");
            notification.setString(1, userid);
            notification.setString(2, strDate);
            ResultSet resultSet = notification.executeQuery();
            while (resultSet.next()) {
                message = resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return message;
    }


    public Notification(String message, String from, String to) {
        this.message = message;
        this.from = from;
        this.to = to;
        message= notifymessage();
    }


    public String getTo(){
        return this.to;
    }
    public String getMessage(){
        return this.message;
    }

}
