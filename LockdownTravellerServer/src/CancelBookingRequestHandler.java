import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CancelBookingRequestHandler extends Handler
{
    Connection connection;
    CancelBookingRequest cb;
    ObjectOutputStream oos;
    CancelBookingRequestHandler(Connection connection,CancelBookingRequest cb,ObjectOutputStream oos)
    {
        this.connection=connection;
        this.cb=cb;
        this.oos=oos;
    }

    @Override
    void sendQuery() throws  SQLException {
        System.out.println("Inside Handler's get response method");
        String PNR=cb.getPNR();
        String query="update booking_info set Booking_Status='Cancelled' where PNR=\""+PNR+"\"";
        CancelBookingResponse cancelBookingResponse = CancelBooking(query);
        Server.SendResponse(oos,cancelBookingResponse);
    }

    public CancelBookingResponse CancelBooking(String query) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        int result=preparedStatement.executeUpdate();
        String response = null;
        if(result==0){response="No bookings found under the given PNR.Failed to cancel the booking";}
        else if(result>0){response="Booking cancelled succesfully";}
        return new CancelBookingResponse(response);
    }
}
