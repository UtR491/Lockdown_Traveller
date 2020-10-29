import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewReroutedTrainsRequestHandler extends Handler implements Serializable {
    Connection connection;
    ObjectOutputStream oos;
    ViewReroutedTrainsRequest viewReroutedTrainsRequest;

    public ViewReroutedTrainsRequestHandler(Connection connection, ObjectOutputStream oos, ViewReroutedTrainsRequest viewReroutedTrainsRequest) {
        this.connection = connection;
        this.oos = oos;
        this.viewReroutedTrainsRequest = viewReroutedTrainsRequest;
    }

    @Override
    void sendQuery() throws  SQLException {
        String q1 = "select Rerouted_Till,Train_ID from Basic_Train_Info where Rerouted_Till is not null;";
        String q2 = "select * from Route_Info where Train_ID=? and inCurrentRoute=1;";
        ViewReroutedTrainsResponse viewReroutedTrainsResponse=viewReroutedTrains(q1,q2);
        Server.SendResponse(oos,viewReroutedTrainsResponse);
    }

    public ViewReroutedTrainsResponse viewReroutedTrains(String q1, String q2) throws SQLException {
        Date currDate=null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String s=sdf.format(new Date());
        try {
            currDate=sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ResultSet resultSet = null,reroutedTrains;
        PreparedStatement preparedStatement;
        ArrayList<String> trainID = new ArrayList<>();
        ArrayList<String>trainName=new ArrayList<>();
        ArrayList<ArrayList<String>>station = new ArrayList<>();
        ArrayList<ArrayList<String>> cityCode=new ArrayList<>();
        ArrayList<ArrayList<String>>arrival=new ArrayList<>();
        ArrayList<ArrayList<String>>departure=new ArrayList<>();
        ArrayList<ArrayList<Integer>>stationNo=new ArrayList<>();
        ArrayList<ArrayList<Integer>>dayNo=new ArrayList<>();
        ArrayList<ArrayList<Integer>>distanceCovered=new ArrayList<>();
        try {
            preparedStatement=connection.prepareStatement(q1);
            resultSet=preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Date reroutedTill;

        assert resultSet != null;
        while (resultSet.next())
        {
            try {
                reroutedTill=sdf.parse(resultSet.getString(1));
                assert currDate != null;
                int compare=reroutedTill.compareTo(currDate);
                if(compare>0)
                {
                    preparedStatement=connection.prepareStatement(q2);
                    preparedStatement.setString(1,resultSet.getString(2));
                    reroutedTrains=preparedStatement.executeQuery();
                    int count=0;
                    ArrayList<String> Station =new ArrayList<>(),CityCode=new ArrayList<>(),Arrival=new ArrayList<>(),Departure=new ArrayList<>();
                    ArrayList<Integer>StationNo = new ArrayList<>(),DaNo=new ArrayList<>(),DistanceCovered=new ArrayList<>();
                    while (reroutedTrains.next())
                    {
                        if(count==0)
                        {
                            trainID.add(reroutedTrains.getString(1));
                            trainName.add(reroutedTrains.getString(2));
                            count=1;
                        }
                        assert false;
                        Station.add(reroutedTrains.getString(3));
                        StationNo.add(reroutedTrains.getInt(4));
                        CityCode.add(reroutedTrains.getString(5));
                        Arrival.add(reroutedTrains.getString(6));
                        Departure.add(reroutedTrains.getString(7));
                        DaNo.add(reroutedTrains.getInt(8));
                        DistanceCovered.add(reroutedTrains.getInt(9));
                    }
                    station.add(Station);
                    stationNo.add(StationNo);
                    cityCode.add(CityCode);
                    arrival.add(Arrival);
                    departure.add(Departure);
                    dayNo.add(DaNo);
                    distanceCovered.add(DistanceCovered);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return new ViewReroutedTrainsResponse(trainID,trainName,station,cityCode,arrival,departure,stationNo,dayNo,distanceCovered);
    }
}

