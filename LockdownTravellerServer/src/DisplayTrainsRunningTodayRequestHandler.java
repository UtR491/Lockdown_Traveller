import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

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
    void sendQuery() throws  SQLException {

        // query to check the expiration of reroute period
        String q1="select Rerouted_Till,Train_ID from Basic_Train_Info where Rerouted_Till is not null;";
        String q2="select Train_ID from Basic_Train_Info where Train_ID=?";
        String q3="delete from Route_Info where Train_ID=? and inCurrentRoute=1;";
        String q4="update Route_Info set inCurrentRoute=1 where Train_ID=? and inCurrentRoute=0;";
        String q5="update Basic_Train_Info set Rerouted_Till = null where Train_ID=?;";
        checkRerouteStatus(q1,q2,q3,q4,q5);

        String query1="select Train_ID,Days_Running,Added_Till,Cancelled_Till from Basic_Train_Info;";
        String query2="select Train_ID,Train_Name,Station,Station_No,Departure,Arrival from Route_Info where Train_ID=? and inCurrentRoute=1;";
        DisplayTrainsRunningTodayResponse displayTrainsRunningTodayResponse=displayTrainsRunningToday(query1,query2);
        Server.SendResponse(oos,displayTrainsRunningTodayResponse);
    }
    private static int DayToDate(String sDate) {
        String[] date = sDate.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek.getValue();
    }
    void checkRerouteStatus(String q1, String q2, String q3, String q4, String q5) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(q1);
        ResultSet resultSet=preparedStatement.executeQuery();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s=sdf.format(new Date());
        Date currDate = null;
        try {
            currDate=sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date reroutedDate = null;
        preparedStatement=connection.prepareStatement(q2);
        while (resultSet.next())
        {
            try {
                reroutedDate=sdf.parse(resultSet.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert reroutedDate != null;
            assert currDate != null;
            int compare=currDate.compareTo(reroutedDate);
            if(compare>0)
            {
                preparedStatement.setString(1,resultSet.getString(2));
                ResultSet trainID=preparedStatement.executeQuery();
                while (trainID.next())
                {
                    preparedStatement=connection.prepareStatement(q3);
                    preparedStatement.setString(1,trainID.getString(1));
                    preparedStatement.executeUpdate();

                    preparedStatement=connection.prepareStatement(q4);
                    preparedStatement.setString(1,trainID.getString(1));
                    preparedStatement.executeUpdate();

                    preparedStatement=connection.prepareStatement(q5);
                    preparedStatement.setString(1,trainID.getString(1));
                    preparedStatement.executeUpdate();
                }

            }


        }
    }
    public DisplayTrainsRunningTodayResponse displayTrainsRunningToday(String query1,String query2)
    {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        ArrayList<String>trainID=new ArrayList<>();
        ArrayList<String>trainName=new ArrayList<>();
        ArrayList<ArrayList<String>>station= new ArrayList<>();
        ArrayList<ArrayList<Integer>>stationNo=new ArrayList<>();
        ArrayList<ArrayList<String>>arrival=new ArrayList<>();
        ArrayList<ArrayList<String>>departure=new ArrayList<>();
        char[]Days_Running;

        //getting todays day in number format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s=sdf.format(new Date());
        int required_day=DisplayTrainsRunningTodayRequestHandler.DayToDate(s);

        try {
            preparedStatement=connection.prepareStatement(query1);
            resultSet=preparedStatement.executeQuery();
            int compare1,compare2,counter=0;
            Date date=sdf.parse(s);

            while (resultSet.next())
            {
                ArrayList<String>Station=new ArrayList<>();
                ArrayList<Integer>StationNo=new ArrayList<>();
                ArrayList<String>Arrival=new ArrayList<>();
                ArrayList<String>Departure=new ArrayList<>();
                Days_Running=resultSet.getString("Days_Running").toCharArray();
                if(Days_Running[required_day-1]=='1')
                {
                    if (resultSet.getString("Added_Till") != null) {
                        java.util.Date date1 = sdf.parse(resultSet.getString("Added_Till"));
                        compare1 = date.compareTo(date1);
                        if (compare1 < 0) {
                            if (resultSet.getString("Cancelled_Till") != null) {
                                java.util.Date date2 = sdf.parse(resultSet.getString("Cancelled_Till"));
                                compare2 = date.compareTo(date2);
                                if (compare2 > 0) {
                                    counter = 1;
                                }
                            } else counter = 1;
                        }
                    } else if (resultSet.getString("Cancelled_Till") != null) {
                        java.util.Date date2 = sdf.parse(resultSet.getString("Cancelled_Till"));
                        compare2 = date.compareTo(date2);
                        if (compare2 > 0) {
                            counter = 1;
                        }
                    } else counter = 1;
                    if(counter==1)
                    {
                        preparedStatement=connection.prepareStatement(query2);
                        preparedStatement.setString(1,resultSet.getString(1));
                        ResultSet trainsRunningToday=preparedStatement.executeQuery();
                        int count=0;
                        while (trainsRunningToday.next()) {
                            if(count==0) {
                                trainID.add(trainsRunningToday.getString(1));
                                trainName.add(trainsRunningToday.getString(2));
                                count=1;
                            }
                            Station.add(trainsRunningToday.getString(3));
                            StationNo.add(trainsRunningToday.getInt(4));
                            Departure.add(trainsRunningToday.getString(5));
                            Arrival.add(trainsRunningToday.getString(6));
                        }
                        station.add(Station);
                        stationNo.add(StationNo);
                        departure.add(Departure);
                        arrival.add(Arrival);
                    }
                }
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return new DisplayTrainsRunningTodayResponse(trainID,trainName,station,stationNo,departure,arrival);
    }
}
