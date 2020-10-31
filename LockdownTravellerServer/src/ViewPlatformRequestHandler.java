import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewPlatformRequestHandler extends Handler {
    private Connection connection;
 private ObjectOutputStream oos;
   private    ViewPlatformRequest viewPlatformRequest;

    public ViewPlatformRequestHandler(Connection connection, ObjectOutputStream oos, ViewPlatformRequest viewPlatformRequest) {
        this.connection = connection;
        this.oos = oos;
        this.viewPlatformRequest = viewPlatformRequest;
    }

    @Override
    void sendQuery()  {
        String query="select * from (select Train_ID,Platform_No,Station_No from Platform_No where Train_ID=? and Date=?) as a " +
                "join" +
                "(select Station,Station_No from Route_Info where Train_ID=? and inCurrentRoute=1) as b " +
                "where a.Station_No=b.Station_No;";
        ViewPlatformResponse viewPlatformResponse=viewPlatform(query,viewPlatformRequest);
        Server.SendResponse(oos,viewPlatformResponse);
    }
    public ViewPlatformResponse viewPlatform(String query,ViewPlatformRequest viewPlatformRequest)
    {
        PreparedStatement preparedStatement;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String s=sdf.format(new Date());
        ResultSet resultSet;
        String trainID = null;
        ArrayList<String>station=new ArrayList<>();
        ArrayList<Integer>stationNo=new ArrayList<>();
        ArrayList<Integer>platformNo=new ArrayList<>();
        try {
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,viewPlatformRequest.getTrainID());
            preparedStatement.setString(2,s);
            preparedStatement.setString(3,viewPlatformRequest.getTrainID());
            resultSet=preparedStatement.executeQuery();
            int count=0;
            while (resultSet.next())
            {
                if(count==0)
                {
                    trainID=resultSet.getString(1);
                    count=1;
                }
                platformNo.add(resultSet.getInt(2));
                stationNo.add(resultSet.getInt(3));
                station.add(resultSet.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ViewPlatformResponse(trainID,station,platformNo,stationNo);
    }
}
