import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class DisplayTrainsRequestHandler extends Handler {
    Connection connection;
    DisplayTrainsRequest displayTrainsRequest;
    ObjectOutputStream oos;

    DisplayTrainsRequestHandler(Connection connection, DisplayTrainsRequest displayTrainsRequest, ObjectOutputStream oos) {
        this.connection = connection;
        this.displayTrainsRequest = displayTrainsRequest;
        this.oos = oos;
    }

    @Override
    void sendQuery() throws SQLException {
        System.out.println("Inside Handler's get response method");
        String source = displayTrainsRequest.getSource();
        String dest = displayTrainsRequest.getDest();
        String sDate = displayTrainsRequest.getsDate();
        //converting the string date into the date datatype of mySQL
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        sDate = LocalDate.parse(sDate, dtf).format(dtf2);
        // query to check the expiration of reroute period
        String q1="select Rerouted_Till,Train_ID from Basic_Train_Info where Rerouted_Till is not null;";
String q2="select Train_ID from Basic_Train_Info where Train_ID=?";
String q3="delete from Route_Info where Train_ID=? and inCurrentRoute=1;";
String q4="update Route_Info set inCurrentRoute=1 where Train_ID=? and inCurrentRoute=0;";
String q5="update Basic_Train_Info set Rerouted_Till = null where Train_ID=?;";
checkRerouteStatus(q1,q2,q3,q4,q5);


    //create a query to find the trains between source and destination
    String query1 = "select x.*, y.Days_Running from\n" +
            "(select a.Train_ID, a.Train_Name, b.Arrival, a.Departure, a.Day_No from \n" +
            "(select * from Route_Info\n" +
            "where Station=? and inCurrentRoute=1) as a\n" +
            "join \n" +
            "(select * from Route_Info\n" +
            "where Station=? and inCurrentRoute=1) as b\n" +
            "where a.Train_ID = b.Train_ID) as x\n" +
            "join \n" +
            "Basic_Train_Info as y\n" +
            "on x.Train_ID = y.Train_ID;\n" +
            "\n";
    //create query to find total seats in each class
    String query2 = "select Sleeper_Coaches,Sleeper_Seats,FirstAC_Coaches,FirstAC_Seats,SecondAC_Coaches,SecondAC_Seats,ThirdAC_Coaches,ThirdAC_Seats,Sleeper_Fare,FirstAC_Fare,SecondAC_Fare,ThirdAC_Fare from Basic_Train_Info where Train_ID=?;";

    String query3 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=? and Seat_No like 'SL%'));";
    String query4 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=?and Seat_No like '1A%'));";
    String query5 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=? and Seat_No like '2A%'));";
    String query6 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=?and Seat_No like '3A%'));";
    String query7 = "select Added_Till,Cancelled_Till from Basic_Train_Info where Train_ID=?;";
    String query8="select Distance_Covered from Route_Info where Train_ID=? and inCurrentRoute=1 and Station in (?,?);";
    DisplayTrainsResponse displayTrainsResponse = DisplayTrains(query1, query2, query3, query4, query5, query6, query7,query8, sDate, source, dest);
    Server.SendResponse(oos, displayTrainsResponse);

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
    private static int DayToDate(String sDate) {
        String[] date = sDate.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek.getValue();
    }

   DisplayTrainsResponse DisplayTrains(String query1, String query2, String query3, String query4, String query5, String query6, String query7,String query8, String sDate, String source, String dest) {
        char[] Days_Running = null;
        ResultSet result = null;
        int required_day = DisplayTrainsRequestHandler.DayToDate(sDate);
        PreparedStatement preparedStatement;
        try {
            assert null != connection;
            preparedStatement = connection.prepareStatement(query1, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1,source);
            preparedStatement.setString(2,dest);
            System.out.println(preparedStatement.toString());

            result = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] Train_ID = new String[5], Train_Name = new String[5], Departure = new String[5], Arrival = new String[5], First_AC = new String[5], Second_AC = new String[5], Third_AC = new String[5], Sleeper = new String[5];
        int i = 0;
       ArrayList<Integer>AC3Fare=new ArrayList<>(),AC2Fare=new ArrayList<>(),AC1Fare=new ArrayList<>(),SLFare=new ArrayList<>();
        while (true) {
            try {
                assert result != null;
                if (!result.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String Day_No = null;
            try {
                Day_No = result.getString("Day_No");
                Days_Running = result.getString("Days_Running").toCharArray();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            assert Days_Running != null;
            assert Day_No != null;
            char value = Days_Running[required_day - Integer.parseInt(Day_No)];
            if (value == '1') {
                try {
                    assert false;
                    preparedStatement = connection.prepareStatement(query7);
                    preparedStatement.setString(1, result.getString("Train_ID"));
                    ResultSet resultSet1 = preparedStatement.executeQuery();
                    resultSet1.next();
                    int compare1, compare2, counter = 0;

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        java.util.Date date = sdf.parse(sDate);
                        if (resultSet1.getString("Added_Till") != null) {
                            java.util.Date date1 = sdf.parse(resultSet1.getString("Added_Till"));
                            compare1 = date.compareTo(date1);
                            if (compare1 < 0) {
                                if (resultSet1.getString("Cancelled_Till") != null) {
                                    java.util.Date date2 = sdf.parse(resultSet1.getString("Cancelled_Till"));
                                    compare2 = date.compareTo(date2);
                                    if (compare2 > 0) {
                                        counter = 1;
                                    }
                                } else counter = 1;
                            }
                        } else if (resultSet1.getString("Cancelled_Till") != null) {
                            java.util.Date date2 = sdf.parse(resultSet1.getString("Cancelled_Till"));
                            compare2 = date.compareTo(date2);
                            if (compare2 > 0) {
                                counter = 1;
                            }
                        } else counter = 1;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (counter == 1) {



                        Train_ID[i] = result.getString("Train_ID");
                        Train_Name[i] = result.getString("Train_Name");
                        Departure[i] = result.getString("Departure");
                        Arrival[i] = result.getString("Arrival");


                        preparedStatement = connection.prepareStatement(query2);
                        preparedStatement.setString(1, Train_ID[i]);
                        ResultSet resultSet = preparedStatement.executeQuery();


                        preparedStatement = connection.prepareStatement(query3);
                        preparedStatement.setString(1, Train_ID[i]);
                        preparedStatement.setString(2, Train_ID[i]);
                        preparedStatement.setString(3, Train_ID[i]);
                        preparedStatement.setString(4, source);
                        preparedStatement.setString(5, Train_ID[i]);
                        preparedStatement.setString(6, dest);
                        preparedStatement.setString(7, sDate);
                        ResultSet SL_Seats = preparedStatement.executeQuery();
                        resultSet.next();
                        SL_Seats.next();
                        int available_seats ;
                        if (resultSet.getString("Sleeper_Seats") != null) {
                            available_seats = Integer.parseInt(resultSet.getString("Sleeper_Seats")) - Integer.parseInt(SL_Seats.getString(1));
                            Sleeper[i] = String.valueOf(available_seats * Integer.parseInt(resultSet.getString("Sleeper_Coaches")));
                        } else Sleeper[i] = "N/A";


                        preparedStatement = connection.prepareStatement(query4);
                        preparedStatement.setString(1, Train_ID[i]);
                        preparedStatement.setString(2, Train_ID[i]);
                        preparedStatement.setString(3, Train_ID[i]);
                        preparedStatement.setString(4, source);
                        preparedStatement.setString(5, Train_ID[i]);
                        preparedStatement.setString(6, dest);
                        preparedStatement.setString(7, sDate);
                        ResultSet AC1_Seats = preparedStatement.executeQuery();
                        AC1_Seats.next();
                        if (resultSet.getString("FirstAC_Seats") != null) {
                            available_seats = Integer.parseInt(resultSet.getString("FirstAC_Seats")) - Integer.parseInt(AC1_Seats.getString(1));
                            First_AC[i] = String.valueOf(available_seats * Integer.parseInt(resultSet.getString("FirstAC_Coaches")));
                        } else First_AC[i] = "N/A";


                        preparedStatement = connection.prepareStatement(query5);
                        preparedStatement.setString(1, Train_ID[i]);
                        preparedStatement.setString(2, Train_ID[i]);
                        preparedStatement.setString(3, Train_ID[i]);
                        preparedStatement.setString(4, source);
                        preparedStatement.setString(5, Train_ID[i]);
                        preparedStatement.setString(6, dest);
                        preparedStatement.setString(7, sDate);
                        ResultSet AC2_Seats = preparedStatement.executeQuery();
                        AC2_Seats.next();
                        if (resultSet.getString("SecondAC_Seats") != null) {
                            available_seats = Integer.parseInt(resultSet.getString("SecondAC_Seats")) - Integer.parseInt(AC2_Seats.getString(1));
                            Second_AC[i] = String.valueOf(available_seats * Integer.parseInt(resultSet.getString("SecondAC_Coaches")));
                        } else Second_AC[i] = "N/A";

                        preparedStatement = connection.prepareStatement(query6);
                        preparedStatement.setString(1, Train_ID[i]);
                        preparedStatement.setString(2, Train_ID[i]);
                        preparedStatement.setString(3, Train_ID[i]);
                        preparedStatement.setString(4, source);
                        preparedStatement.setString(5, Train_ID[i]);
                        preparedStatement.setString(6, dest);
                        preparedStatement.setString(7, sDate);
                        ResultSet AC3_Seats = preparedStatement.executeQuery();
                        AC3_Seats.next();
                        if (resultSet.getString("ThirdAC_Seats") != null) {
                            available_seats = Integer.parseInt(resultSet.getString("ThirdAC_Seats")) - Integer.parseInt(AC3_Seats.getString(1));
                            Third_AC[i] = String.valueOf(available_seats * Integer.parseInt(resultSet.getString("ThirdAC_Coaches")));
                        } else Third_AC[i] = "N/A";
                        i++;



                        //getting the tkt cost for each class
                        preparedStatement=connection.prepareStatement(query8);
                        preparedStatement.setString(1,result.getString("Train_ID"));
                        preparedStatement.setString(2,source);
                        preparedStatement.setString(3,dest);
                        ResultSet d=preparedStatement.executeQuery();
                        Integer distance=d.getInt(2)-d.getInt(1);
                        if(result.getString(12)!=null)AC3Fare.add(result.getInt(12)*distance);
                        else AC3Fare.add(-1);
                        if(result.getString(11)!=null)AC2Fare.add(result.getInt(11)*distance);
                        else AC2Fare.add(-1);
                        if(result.getString(10)!=null)AC1Fare.add(result.getInt(10)*distance);
                        else AC1Fare.add(-1);
                        if(result.getString(9)!=null)SLFare.add(result.getInt(9)*distance);
                        else SLFare.add(-1);




                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            }
        return new DisplayTrainsResponse(Train_ID, Train_Name, source, Departure, dest, Arrival, First_AC, Second_AC, Third_AC, Sleeper, sDate, i);

    }
}