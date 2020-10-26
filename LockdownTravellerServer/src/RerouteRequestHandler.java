import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RerouteRequestHandler extends Handler {
    private ObjectOutputStream objectOutputStream;
    private RerouteRequest rerouteRequest;
    private  Connection connection;
    RerouteRequestHandler(Connection connection,RerouteRequest rerouteRequest,ObjectOutputStream objectOutputStream)
    {
        this.connection=connection;
        this.objectOutputStream=objectOutputStream;
        this.rerouteRequest=rerouteRequest;
    }

    /*
    1. If we a replacing the last station with some other station, we don't need nextStation and nextDistance.
    2. If we are replacing the first station with some other station, we don't need previousStation and prevDistance.
    3. If we are replacing a middle station, we need all the inputs.
    4. If we are extending the route, we only need the currentStation and nothing else.

    Case 1. distanceNext and nextStation is null. No need of distanceOld.
    Case 2. distancePrev and prevStation is null. No need of distanceOld.
    Case 3. nothing is null. Well we don't need distanceOld.
    Case 4. distanceNext, nextStation, distancePrev, prevStation are null. We primarily need oldStation and distanceOld.
     */
    @Override
    void sendQuery()  {
        String query1="update Basic_Train_Info set Rerouted_Till=? where Train_ID=?;";
        String query2="select Distance_Covered,Train_Name,Station_No from Route_Info where Train_ID=? and Station in (?,?);";
        System.out.println(query2);
        String query3="insert into Route_Info values (?,?,?,?,?,?,?,?,?,1);";
        String query4="update Route_Info set Distance_Covered=? where Station=? and Train_ID=?;";
        String query5="update Route_Info set Distance_Covered=Distance_Covered+? where Station in (select Station from Route_Info where Train_ID=? and Station_No>?);";
        String query6="update Route_Info set inCurrentRoute=? where Train_ID=? and Station=?;";
        String query7="update notification set Message=?,Pending_Status=1,User_ID=User_ID in (select distinct User_ID from User );";
        RerouteResponse rerouteResponse=rerouteTrains(query1,query2,query3,query4,query5,query6,query7);
        Server.SendResponse(objectOutputStream,rerouteResponse);

    }
    public RerouteResponse rerouteTrains(String query1,String query2,String query3,String query4,String query5,String query6,String query7)
    {
        String date=rerouteRequest.getsDate();
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtf2=  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date= LocalDate.parse(date,dtf).format(dtf2);
        String response = null;
        try {
            PreparedStatement updateBasicTrainInfo = connection.prepareStatement(query1);
            updateBasicTrainInfo.setString(1, date);
            updateBasicTrainInfo.setString(2, rerouteRequest.Train_ID);
            updateBasicTrainInfo.executeUpdate();
            if(rerouteRequest.getInplace()) {
                // Case 1, 2 and 3 are here
                if(rerouteRequest.getNextStation() == null) {
                    // Case 1.
                    PreparedStatement replaceLastStation = connection.prepareStatement("");
                } else if (rerouteRequest.getPrevStation() == null) {

                } else {

                }

            } else {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new RerouteResponse(response);
    }

}
