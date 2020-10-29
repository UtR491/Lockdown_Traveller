import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class DisplayTrainsRunningTodayRequestHandler extends Handler{
Connection connection;
ObjectOutputStream oos;
DisplayTrainsRunningTodayRequest displayTrainsRunningTodayRequest;

    public DisplayTrainsRunningTodayRequestHandler(Connection connection, ObjectOutputStream oos, DisplayTrainsRunningTodayRequest displayTrainsRunningTodayRequest) {
        this.connection = connection;
        this.oos = oos;
        this.displayTrainsRunningTodayRequest = displayTrainsRunningTodayRequest;
    }

    @Override
    void sendQuery() throws IOException, SQLException {
        String query1="select Train_ID,Days_Running,Added_Till,Cancelled_Till,Rerouted_Till from Basic_Train_Info;";
        String query2="select Train_ID,Train_Name,Station,Station_No "
    }
}
