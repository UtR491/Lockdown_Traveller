import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SetPlatformRequestHandler extends Handler {
    private Connection connection;
    private ObjectOutputStream oos;
    private SetPlatformRequest setPlatformRequest;

    public SetPlatformRequestHandler(Connection connection, ObjectOutputStream oos, SetPlatformRequest setPlatformRequest) {
        this.connection = connection;
        this.oos = oos;
        this.setPlatformRequest = setPlatformRequest;
    }

    @Override
    void sendQuery() {
        String query1="insert into Platform_No values(?,?,?,?);";
        String query2="select distinct User_ID from Booking_Info where Booking_ID in (select distinct Booking_ID from Vacancy_Info where Train_ID=? and Date=?);";
        String query3="insert into Notification values(?,?,1);";
        SetPlatformResponse setPlatformResponse=setPlatform(query1,query2,query3,setPlatformRequest);
        System.out.println("Sending the set platform request.");
        Server.SendResponse(oos,setPlatformResponse);
    }
    public SetPlatformResponse setPlatform(String query1,String query2,String query3,SetPlatformRequest setPlatformRequest){
        PreparedStatement preparedStatement;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(new Date());
        for(int i=0;i<setPlatformRequest.getTrainID().size();i++)
        {
            try {
                preparedStatement=connection.prepareStatement(query1);
                preparedStatement.setString(1,setPlatformRequest.getTrainID().get(i));
                preparedStatement.setString(4,date);
                for(int j=0;j<setPlatformRequest.getPlatformNo().get(i).size();j++)
                {
                    preparedStatement.setInt(2,setPlatformRequest.getStationNo().get(i).get(j));
                    preparedStatement.setInt(3,setPlatformRequest.getPlatformNo().get(i).get(j));
                    int c=preparedStatement.executeUpdate();
                    if(c==0){return new SetPlatformResponse("failure");}
                }

                preparedStatement=connection.prepareStatement(query2);
                preparedStatement.setString(1,setPlatformRequest.getTrainID().get(i));
                preparedStatement.setString(2,date);
                ResultSet resultSet=preparedStatement.executeQuery();
                while (resultSet.next())
                {
                    preparedStatement=connection.prepareStatement(query3);
                    preparedStatement.setString(1,resultSet.getString(1));
                    preparedStatement.setString(2,"Platforms for Train Number "+setPlatformRequest.getTrainID().get(i)+"have been updated");
                    int d=preparedStatement.executeUpdate();
                    if(d==0){return  new SetPlatformResponse("failure");}
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new SetPlatformResponse("success");

    }
}
