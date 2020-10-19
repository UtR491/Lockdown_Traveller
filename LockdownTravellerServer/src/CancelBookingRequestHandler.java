
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

public class CancelBookingRequestHandler extends Handler
{
    DatabaseConnector db;
    CancelBookingRequest cb;
    ObjectOutputStream oos;
    CancelBookingRequestHandler(DatabaseConnector db,CancelBookingRequest cb,ObjectOutputStream oos)
    {
        this.db=db;
        this.cb=cb;
        this.oos=oos;
    }

    @Override
    void sendQuery() throws IOException, SQLException {
        System.out.println("Inside Handler's get response method");
        String PNR=cb.getPNR();
        String query="update booking_info set Booking_Status='Cancelled' where PNR=\""+PNR+"\"";
        CancelBookingResponse cancelBookingResponse = db.CancelBooking(query,oos);
        CancelBookingResponse.SendResponse(oos, cancelBookingResponse);
    }
}
