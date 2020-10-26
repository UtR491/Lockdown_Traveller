import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddTrainsRequestHandler extends Handler {
    final private AddTrainsRequest addTrainsRequest;
    final private Connection connection;
    final private ObjectOutputStream oos;
    public AddTrainsRequestHandler(Connection connection, AddTrainsRequest addTrainsRequest,ObjectOutputStream oos) {
       this.connection=connection;
       this.addTrainsRequest=addTrainsRequest;
       this.oos=oos;
    }

    String date;

    @Override
    void sendQuery() {
        date= addTrainsRequest.getAdded_Till();
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtf2=  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(date != null)
        date= LocalDate.parse(date,dtf).format(dtf2);
        String query1 = "insert into Basic_Train_Info values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String query2 = "insert into Route_Info values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        AddTrainsResponse addTrainsResponse=addTrains(query1, query2);
        Server.SendResponse(oos,addTrainsResponse);
    }
    public AddTrainsResponse addTrains(String query1, String query2)
    {
        String response=null;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query1);
            preparedStatement.setString(1, addTrainsRequest.getTrain_ID());
            preparedStatement.setString(2, addTrainsRequest.getTrain_Name());
            preparedStatement.setString(3, addTrainsRequest.getDays_Running());
            preparedStatement.setString(4, addTrainsRequest.getFirstAC_Coaches());
            preparedStatement.setString(5, addTrainsRequest.getSecondAC_Coaches());
            preparedStatement.setString(6, addTrainsRequest.getThirdAC_Coaches());
            preparedStatement.setString(7, addTrainsRequest.getSleeper_Coaches());
            preparedStatement.setString(8, addTrainsRequest.getFirstAC_Seats());
            preparedStatement.setString(9, addTrainsRequest.getSecondAC_Seats());
            preparedStatement.setString(10, addTrainsRequest.getThirdAC_Seats());
            preparedStatement.setString(11, addTrainsRequest.getSleeper_Seats());
            preparedStatement.setString(12, addTrainsRequest.getFirstAC_Fare());
            preparedStatement.setString(13, addTrainsRequest.getSecondAC_Fare());
            preparedStatement.setString(14, addTrainsRequest.getThirdAC_Fare());
            preparedStatement.setString(15, addTrainsRequest.getSleeper_Fare());
            preparedStatement.setString(16, date);
            int c1=preparedStatement.executeUpdate();

            PreparedStatement addRouteInfo = connection.prepareStatement(query2);

            int c2 = 1;
            for(int i = 0; i < addTrainsRequest.getStation().size(); i++) {
                addRouteInfo.setString(1, addTrainsRequest.getTrain_ID());
                addRouteInfo.setString(2, addTrainsRequest.getTrain_Name());
                addRouteInfo.setString(3, addTrainsRequest.getStation().get(i));
                addRouteInfo.setInt(4, i);
                addRouteInfo.setString(5, addTrainsRequest.getCity_Code().get(i));
                addRouteInfo.setString(6, addTrainsRequest.getArrival().get(i));
                addRouteInfo.setString(7, addTrainsRequest.getDeparture().get(i));
                addRouteInfo.setString(8, addTrainsRequest.getDay_No().get(i));
                addRouteInfo.setString(9, addTrainsRequest.getDistance_Covered().get(i));
                c2 = c2 * addRouteInfo.executeUpdate();
            }

            if(c1*c2!=0){
                System.out.println("Train was added response is success");
                response="success";
            }
            else{response="failure";}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new AddTrainsResponse(response);
    }
}
