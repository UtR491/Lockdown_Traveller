import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseConnector {
    private Connection connection = null;

    public DatabaseConnector() {
        createConnection();
    }

    public BookingResponse bookingRequest(String query1, String query2, String query3, String query4,
                                          int numSeat, int availableSeat, String query5, long[] bookingIds,
                                          String[] preference, int[] age, char[] gender) {
        ResultSet conflict;
        ResultSet stationsOnRoute;
        ResultSet seatInCoach;
        StringBuilder inClause = new StringBuilder("(");
        List<String> route = new ArrayList<>();
        boolean first = true;
        int firstStationNo = 1;
        try {
            stationsOnRoute = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(query2);
            while(stationsOnRoute.next()) {
                if(first) {
                    first = false;
                    firstStationNo = stationsOnRoute.getInt("Station_No");
                }
                System.out.println(stationsOnRoute.getString("Station"));
                inClause.append("'").append(stationsOnRoute.getString("Station")).append("', ");
                route.add(String.valueOf(stationsOnRoute.getString("Station")));
            }
            inClause.append("'')");

            query1 = query1 + "and Station in " + inClause + ";";
            System.out.println(query1);
            conflict = connection.createStatement().executeQuery(query1);

            seatInCoach = connection.createStatement().executeQuery(query3);
            System.out.println(query3);
            int max = 0;
            while(seatInCoach.next())
                max = seatInCoach.getInt("Sleeper_Seats");
            boolean[] isOccupied = new boolean[max+1];
            while(conflict.next()) {
                System.out.println(conflict.getString("Seat_No"));
                isOccupied[Integer.parseInt(conflict.getString("Seat_No").substring(2))] = true;
            }
            for(int i = 0; i < numSeat-availableSeat; ++i) {
                query4 = query4.replaceFirst("confirmed", "waiting");
            }
            String[] seatsAlloted = new String[numSeat];
            connection.createStatement().executeUpdate(query4);
            int confirmedSeats = 0;
            int idx = 0;
            route.remove(route.size()-1);
/*
            for(int i=1;i<=max;i++) {
                if(!isOccupied[i]) {
                    for(String station : route) {
                        String tempQuery = query5.replaceFirst("xxxxxxxxxx", String.valueOf(bookingIds[confirmedSeats]));
                        tempQuery = tempQuery.replaceFirst("station", station);
                        String seat;
                        if(i<10)
                            seat = "0"+i;
                        else
                            seat = String.valueOf(i);
                        tempQuery = tempQuery.replaceFirst("xx", seat);
                        tempQuery = tempQuery.replaceFirst("'stationNo'", String.valueOf(firstStationNo++));

                        seatsAlloted[confirmedSeats] = seat;

                        connection.createStatement().executeUpdate(tempQuery);
                    }
                    if(++confirmedSeats == Math.min(availableSeat, numSeat))
                        break;
                }
            }
*/
            while(confirmedSeats < Math.min(availableSeat, numSeat)) {
                if(gender[confirmedSeats] == 'F') {
                    if(age[confirmedSeats] < 11) {

                    }
                    else if(age[confirmedSeats] < 21) {

                    }
                    else if(age[confirmedSeats] < 51) {

                    }
                    else {

                    }
                }
                else {
                    if(age[confirmedSeats] < 11) {

                    }
                    else if(age[confirmedSeats] < 21) {

                    }
                    else if(age[confirmedSeats] < 51) {

                    }
                    else {

                    }
                }
                confirmedSeats++;
            }
            return new BookingResponse(bookingIds, seatsAlloted, confirmedSeats);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    private static int DayToDate(String sDate) {
        String[] date = sDate.split("/");
        System.out.println("Printing stuff"+date[0]);
        System.out.println(date[1]);
        System.out.println(date[2]);
        LocalDate localDate = LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek.getValue();
    }
    DisplayTrainsResponse DisplayTrains (String query1,String query2,String query3,String query4,String query5,String query6,String query7,String sDate,String source,String dest) throws IOException {
        char[] Days_Running = null;
        ResultSet result = null;
        int required_day = DatabaseConnector.DayToDate(sDate);
        PreparedStatement preparedStatement;
        try {
            assert null != connection;
            preparedStatement = connection.prepareStatement(query1, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            result = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String [] Train_ID = new String[5],Train_Name = new String[5],Departure=new String[5],Arrival=new String[5],First_AC=new String[5],Second_AC=new String[5],Third_AC=new String[5],Sleeper=new String[5];
        String Date = null,Source=null,Destination=null;
        int i=0;
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
                    String q6=query7.replaceFirst("xxxxx",result.getString("Train_ID"));
                    preparedStatement=connection.prepareStatement(q6);
                    ResultSet resultSet1=preparedStatement.executeQuery();
                    resultSet1.next();
                    int compare1=0,compare2=0,counter=0;

                    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        java.util.Date date=sdf.parse(sDate);
                        if(resultSet1.getString("Added_Till")!=null)
                        {
                            java.util.Date date1= sdf.parse(resultSet1.getString("Added_Till"));
                            compare1= date.compareTo(date1);
                            if(compare1<0)
                            {
                                if(resultSet1.getString("Cancelled_Till")!=null)
                                {
                                    java.util.Date date2= sdf.parse(resultSet1.getString("Cancelled_Till"));
                                    compare2=date.compareTo(date2);
                                    if(compare2>0)
                                    {
                                        counter=1;
                                    }
                                }
                                else counter=1;
                            }
                        }
                        else counter=1;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (counter==1) {
                        Train_ID[i] = result.getString("Train_ID");
                        Train_Name[i] = result.getString("Train_Name");
                        Source = source;
                        Departure[i] = result.getString("Departure");
                        Destination = dest;
                        Arrival[i] = result.getString("Arrival");
                        Date = sDate;


                        String q = query2.replaceFirst("xxxxx", Train_ID[i]);
                        preparedStatement = connection.prepareStatement(q);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        String q2 = query3.replaceFirst("xxxxx", Train_ID[i]);
                        preparedStatement = connection.prepareStatement(q2);
                        ResultSet SL_Seats = preparedStatement.executeQuery();
                        resultSet.next();
                        SL_Seats.next();
                        int available_seats = 0;
                        if (resultSet.getString("Sleeper_Seats") != null) {
                            available_seats = Integer.parseInt(resultSet.getString("Sleeper_Seats")) - Integer.parseInt(SL_Seats.getString(1));
                            Sleeper[i] = String.valueOf(available_seats*Integer.parseInt(resultSet.getString("Sleeper_Coaches")));
                        }
                        else Sleeper[i]=String.valueOf(available_seats);



                        String q3 = query4.replaceFirst("xxxxx", Train_ID[i]);

                        preparedStatement = connection.prepareStatement(q3);
                        ResultSet AC1_Seats = preparedStatement.executeQuery();
                        AC1_Seats.next();
                        if (resultSet.getString("FirstAC_Seats") != null) {
                            available_seats = Integer.parseInt(resultSet.getString("FirstAC_Seats")) - Integer.parseInt(AC1_Seats.getString(1));
                            First_AC[i] = String.valueOf(available_seats * Integer.parseInt(resultSet.getString("FirstAC_Coaches")));
                        }
                        else First_AC[i] = String.valueOf(available_seats);

                        String q4 = query5.replaceFirst("xxxxx", Train_ID[i]);
                        preparedStatement = connection.prepareStatement(q4);
                        ResultSet AC2_Seats = preparedStatement.executeQuery();
                        AC2_Seats.next();
                        if (resultSet.getString("SecondAC_Seats") != null) {
                            available_seats = Integer.parseInt(resultSet.getString("SecondAC_Seats")) - Integer.parseInt(AC2_Seats.getString(1));
                            Second_AC[i] = String.valueOf(available_seats * Integer.parseInt(resultSet.getString("SecondAC_Coaches")));
                        }
                        else Second_AC[i] = String.valueOf(available_seats);


                        String q5 = query6.replaceFirst("xxxxx", Train_ID[i]);
                        preparedStatement = connection.prepareStatement(q5);
                        ResultSet AC3_Seats = preparedStatement.executeQuery();
                        AC3_Seats.next();
                        if (resultSet.getString("ThirdAC_Seats") != null) {
                            available_seats = Integer.parseInt(resultSet.getString("ThirdAC_Seats")) - Integer.parseInt(AC3_Seats.getString(1));
                            Third_AC[i] = String.valueOf(available_seats * Integer.parseInt(resultSet.getString("ThirdAC_Coaches")));
                        }
                        else Third_AC[i] = String.valueOf(available_seats);
                        i++;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
         return new DisplayTrainsResponse(Train_ID,Train_Name,Source,Departure,Destination,Arrival,First_AC,Second_AC,Third_AC,Sleeper,Date,i);

    }
    public CancelBookingResponse CancelBooking(String query, ObjectOutputStream oos) throws SQLException, IOException {
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        int result=preparedStatement.executeUpdate();
        String response = null;
        if(result==0){response="No bookings found under the given PNR.Failed to cancel the booking";}
        else if(result>0){response="Booking cancelled succesfully";}
        return new CancelBookingResponse(response);
    }

    private void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream("./src/db.properties")));
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/lockdown_traveller", properties);

        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public AdminLoginResponse adminLoginRequest(String query) {
        try {
            System.out.println("Prepared statement");
            PreparedStatement validateLogin = connection.prepareStatement(query);
            System.out.println("going to execute");
            ResultSet adminCredentials = validateLogin.executeQuery();
            if (!adminCredentials.next()) {
                System.out.println("fail");
                return new AdminLoginResponse("failure");
            } else {
                do {
                    System.out.println("success");
                    return new AdminLoginResponse("success");
                } while (adminCredentials.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("null");
        return null;
    }
    public RemoveTrainsResponse removeTrainsRequest(String query1,String query2,String query3)
    {
        int cancelStatus=0;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query1);
             cancelStatus=preparedStatement.executeUpdate();
             if(cancelStatus!=0)
             {
                 preparedStatement=connection.prepareStatement(query2);
                 ResultSet resultSet=preparedStatement.executeQuery();
                 while (resultSet.next())
                 {
                     String q=query2.replaceFirst("xxxxx",resultSet.getString("User_ID"));
                     preparedStatement=connection.prepareStatement(q);
                     cancelStatus=preparedStatement.executeUpdate();
                 }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new RemoveTrainsResponse(cancelStatus);
    }
    public CancelTrainsResponse cancelTrains(String query1,String query2,String query3)
    {
        String reponse=null;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query1);
            int c=preparedStatement.executeUpdate();
            if(c!=0){
                preparedStatement=connection.prepareStatement(query2);
                ResultSet resultSet=preparedStatement.executeQuery();
                while (resultSet.next())
                {
                    String q=query3.replaceFirst("xxxxx",resultSet.getString("User_ID"));
                    preparedStatement=connection.prepareStatement(q);
                    c=preparedStatement.executeUpdate();
                }
            }
            if(c!=0){reponse="Train Cancelled Succesfully";}
            else {reponse="Could not find train";}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new CancelTrainsResponse(reponse);
    }
    public AddTrainsResponse addTrains(String query)
    {
        String reponse=null;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            int c=preparedStatement.executeUpdate();
            if(c!=0){reponse="Train added succesfully";}
            else{reponse="Error occured";}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new AddTrainsResponse(reponse);

    }
    public AddSeatsResponse addSeats(String query1,String query2,int numOfSeats)
    {
        String response=null;
        PreparedStatement preparedStatement= null;
        int c=0,seats=0;
        try {
            preparedStatement = connection.prepareStatement(query1);
            ResultSet resultSet=preparedStatement.executeQuery();
             seats=numOfSeats+resultSet.getInt(1);
            String q=query2.replaceFirst("xx", String.valueOf(seats));
            preparedStatement=connection.prepareStatement(q);
             c=preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(c!=0){response="Total number of seats after addition are"+ seats;}
        else {response="Could not add seats,Please try again";}
        return new AddSeatsResponse(response);

    }
    public RemoveSeatsResponse removeSeats(String query1,String query2,int numOfSeats)
    {
        String response=null;
        PreparedStatement preparedStatement= null;
        int c=0,seats=0;
        try {
            preparedStatement = connection.prepareStatement(query1);
            ResultSet resultSet=preparedStatement.executeQuery();
            seats=resultSet.getInt(1)-numOfSeats;
            if(seats>=0)
            {String q=query2.replaceFirst("xx", String.valueOf(seats));
            preparedStatement=connection.prepareStatement(q);
            c=preparedStatement.executeUpdate();}
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(c!=0){response="Total number of seats after removal are"+ seats;}
        else {response="Could not remove seats,Please try again";}
        return new RemoveSeatsResponse(response);
    }
    public AddCoachesResponse addCoaches(String query1,String query2,int numOfCoaches)
    {
        String response=null;
        PreparedStatement preparedStatement= null;
        int c=0,coaches=0;
        try {
            preparedStatement = connection.prepareStatement(query1);
            ResultSet resultSet=preparedStatement.executeQuery();
            coaches=numOfCoaches+resultSet.getInt(1);
            String q=query2.replaceFirst("xx", String.valueOf(coaches));
            preparedStatement=connection.prepareStatement(q);
            c=preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(c!=0){response="Total number of seats after addition are"+ coaches;}
        else {response="Could not add seats,Please try again";}
        return new AddCoachesResponse(response);
    }

    public RemoveCoachesResponse removeCoaches(String query1, String query2, int numOfCoaches) {
        String response=null;
        PreparedStatement preparedStatement= null;
        int c=0,coaches=0;
        try {
            preparedStatement = connection.prepareStatement(query1);
            ResultSet resultSet=preparedStatement.executeQuery();
            coaches=resultSet.getInt(1)-numOfCoaches;
            if(coaches>=0)
            {String q=query2.replaceFirst("xx", String.valueOf(coaches));
                preparedStatement=connection.prepareStatement(q);
                c=preparedStatement.executeUpdate();}
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(c!=0){response="Total number of coaches after removal are"+ coaches;}
        else {response="Could not remove coaches,Please try again";}
        return new RemoveCoachesResponse(response);
    }
}
