import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaintainSeatsRequestHandler {
    ObjectOutputStream oos;
    Connection connection;
    MaintainSeatsRequest maintainSeatsRequest;
    public MaintainSeatsRequestHandler(ObjectOutputStream oos, Connection connection, MaintainSeatsRequest request) {
        this.oos = oos;
        this.connection = connection;
        this.maintainSeatsRequest = request;
    }

    public void sendQuery() {
        String query = "select * from Basic_Train_Info;";
        MaintainSeatsResponse maintainSeatsResponse = maintainSeatsRequest(query);
        Server.SendResponse(oos, maintainSeatsResponse);
    }

    private MaintainSeatsResponse maintainSeatsRequest(String query) {
        try {
            PreparedStatement seatInquiry = connection.prepareStatement(query);
            ResultSet seatInformation = seatInquiry.executeQuery();
            ArrayList<Train2> trains = new ArrayList<>();
            while(seatInformation.next()) {
                ArrayList<Coach> coaches = new ArrayList<>();
                coaches.add(new Coach("Sleeper", seatInformation.getInt("Sleeper_Coaches"), seatInformation.getInt("Sleeper_Seats")));
                coaches.add(new Coach("First AC", seatInformation.getInt("FirstAC_Coaches"), seatInformation.getInt("FirstAC_Seats")));
                coaches.add(new Coach("Second AC", seatInformation.getInt("SecondAC_Coaches"), seatInformation.getInt("SecondAC_Seats")));
                coaches.add(new Coach("Third AC", seatInformation.getInt("ThirdAC_Coaches"), seatInformation.getInt("ThirdAC_Seats")));

                trains.add(new Train2(seatInformation.getString("Train_ID"), seatInformation.getString("Train_Name"), coaches));
            }
            return new MaintainSeatsResponse(trains);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
