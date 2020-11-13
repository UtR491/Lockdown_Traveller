import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaintainTrainsRequestHandler extends Handler{
    Connection connection;
    MaintainTrainsRequest maintainTrainsRequest;
    ObjectOutputStream oos;
    public MaintainTrainsRequestHandler(ObjectOutputStream oos, Connection connection, MaintainTrainsRequest request) {
        this.connection = connection;
        this.maintainTrainsRequest = request;
        this.oos = oos;
    }
    @Override
    public void sendQuery() {
        System.out.println("Inside sendQuery of maintainTrainsResponse");
        String query1 = "select * from Basic_Train_Info;";
        String query2 = "select * from Route_Info where Train_ID = ? and inCurrentRoute = 1;";
        MaintainTrainsResponse maintainTrainsResponse = maintainTrainsRequest(query1, query2);
        Server.SendResponse(oos, maintainTrainsResponse);
    }
    private @Nullable MaintainTrainsResponse maintainTrainsRequest(String query1, String query2) {
        try {
            PreparedStatement basicInfoQuery = connection.prepareStatement(query1);
            ResultSet basicTrainInfo = basicInfoQuery.executeQuery();
            ArrayList<Train> trains = new ArrayList<>();
            ArrayList<String> route;
            ArrayList<String> arrival;
            ArrayList<String> departure;
            ArrayList<Integer> day;
            ArrayList<Integer> stationNumber;

            int index = 0;
            while(basicTrainInfo.next()) {
                PreparedStatement routeQuery = connection.prepareStatement(query2);
                routeQuery.setString(1, basicTrainInfo.getString("Train_ID"));

                @SuppressWarnings("assignment.type.incompatible") // Train_ID, Train_Name, Days_Running cannot be null. Refer the README.
                @NonNull String trainId = basicTrainInfo.getString("Train_ID"),
                        trainName = basicTrainInfo.getString("Train_Name"),
                        days = basicTrainInfo.getString("Days_Running");

                ResultSet routeInfo = routeQuery.executeQuery();
                route = (new ArrayList<>());
                arrival = (new ArrayList<>());
                departure = (new ArrayList<>());
                day = new ArrayList<>();
                stationNumber = new ArrayList<>();
                while(routeInfo.next()) {
                    @SuppressWarnings("assignment.type.incompatible") // Station cannot be null. Refer the README.
                    @NonNull String pRoute = routeInfo.getString("Station");
                    @Nullable String pArrival = routeInfo.getString("Arrival"),
                            pDeparture = routeInfo.getString("Departure");
                    route.add(pRoute);
                    arrival.add(pArrival != null ? pArrival : "Start");
                    departure.add(pDeparture != null ? pDeparture : "End");
                    day.add(routeInfo.getInt("Day_No"));
                    stationNumber.add(routeInfo.getInt("Station_No"));
                }
                trains.add(new Train(trainId, trainName, days, route, arrival, departure, stationNumber, day));
            }
            return new MaintainTrainsResponse(trains);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
