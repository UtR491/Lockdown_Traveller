import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaintainSeatsRequestHandler extends Handler {

    final private ObjectOutputStream oos;
    final private Connection connection;
    final private MaintainSeatsRequest maintainSeatsRequest;

    /**
     * Constructor. Initializes the object with the database connection, request object and output stream to send the
     * response.
     * @param oos Output stream to send the object.
     * @param connection Connection to the database.
     * @param request Maintain seats request object read from the input stream.
     */
    public MaintainSeatsRequestHandler(ObjectOutputStream oos, Connection connection, MaintainSeatsRequest request) {
        this.oos = oos;
        this.connection = connection;
        this.maintainSeatsRequest = request;
    }

    /**
     * This function is called first.
     */
    @Override
    public void sendQuery() {
        // Get all details from basic train info where coach and seat information is also stored.
        String query = "select * from Basic_Train_Info;";
        MaintainSeatsResponse maintainSeatsResponse = maintainSeatsRequest(query);
        Server.SendResponse(oos, maintainSeatsResponse);
    }

    /**
     * Response by the server to the request object.
     * @param query Query to get all the information.
     * @return Response to the maitatin seats request.
     */
    private @Nullable MaintainSeatsResponse maintainSeatsRequest(String query) {
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
                @SuppressWarnings("assignment.type.incompatible") // Train_ID cannot be null. Refer the README.
                @NonNull String trainId = seatInformation.getString("Train_ID"),
                        trainName = seatInformation.getString("Train_Name");
                trains.add(new Train2(trainId, trainName, coaches));
            }
            return new MaintainSeatsResponse(trains);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
