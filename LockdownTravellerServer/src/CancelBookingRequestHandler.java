import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CancelBookingRequestHandler extends Handler
{
    Connection connection;
    CancelBookingRequest cb;

    CancelBookingRequestHandler(Connection connection,CancelBookingRequest cb)
    {
        this.connection=connection;
        this.cb=cb;
    }

    @Override
    void sendQuery() throws IOException, SQLException {
        System.out.println("Inside Handler's get response method");
        String PNR=cb.getPNR();
        String query="update booking_info set Booking_Status='Cancelled' where PNR=\""+PNR+"\"";
        CancelBookingResponse cancelBookingResponse = CancelBooking(query);
        Server.SendResponse(cancelBookingResponse);
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
