import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

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
import java.util.Objects;

public class DisplayTrainsRequestHandler extends Handler {

    final private Connection connection;
    final private DisplayTrainsRequest displayTrainsRequest;
    final private ObjectOutputStream oos;

    /**
     * Constructor. Initializes the object with the database connection, request object and output stream to send the
     * response.
     * @param connection The connection to the database to execute updates and queries.
     * @param displayTrainsRequest The booking request object with relevant information.
     * @param oos The output stream to send the object.
     */
    DisplayTrainsRequestHandler(Connection connection, DisplayTrainsRequest displayTrainsRequest, ObjectOutputStream oos) {
        this.connection = connection;
        this.displayTrainsRequest = displayTrainsRequest;
        this.oos = oos;
    }

    /**
     * This is the first function that is called inside the request identifier. In this function, we form the relevant
     * sql queries and pass it to another function to execute.
     */
    @Override
    void sendQuery() {
        System.out.println("Inside Handler's get response method");
        String source = displayTrainsRequest.getSource();
        String dest = displayTrainsRequest.getDest();
        String sDate = displayTrainsRequest.getsDate();
        //converting the string date into the date datatype of mySQL
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        sDate = LocalDate.parse(sDate, dtf).format(dtf2);

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
        // Create query to find total seats in each class
        String query2 = "select Sleeper_Coaches,Sleeper_Seats,FirstAC_Coaches,FirstAC_Seats,SecondAC_Coaches,SecondAC_Seats,ThirdAC_Coaches,ThirdAC_Seats,Sleeper_Fare,FirstAC_Fare,SecondAC_Fare,ThirdAC_Fare from Basic_Train_Info where Train_ID=?;";
        // Query for each type of coach, to count the number of seats available.
        // TODO: Replace with a single query.
        String query3 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=? and Seat_No like 'SL%'));";
        String query4 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in  (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=?and Seat_No like 'A1%'));";
        String query5 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=? and Seat_No like 'A2%'));";
        String query6 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=?and Seat_No like 'A3%'));";
        // State of the train.
        // NOT NEEDED IN THE MINIMAL VERSION, SINCE WE DON'T PROVIDE REROUTE AND CANCEL FUNCTIONALITY HERE.
        String query7 = "select Added_Till,Cancelled_Till from Basic_Train_Info where Train_ID=?;";
        // To find fare of the journey.
        String query8="select Distance_Covered from Route_Info where Train_ID=? and inCurrentRoute=1 and Station in (?,?);";
        DisplayTrainsResponse displayTrainsResponse = DisplayTrains(query1, query2, query3, query4, query5, query6, query7,query8, sDate, source, dest);
        Server.SendResponse(oos, displayTrainsResponse);

    }

    /**
     * Change string date to the day of week.
     * @param sDate Date.
     * @return Day of week.
     */
    private static int DayToDate(String sDate) {
        String[] date = sDate.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek.getValue();
    }

    /**
     * Execute queries and return the response object.
     * @params Queries.
     * @param sDate Date in string.
     * @param source Source.
     * @param dest Destination.
     * @return The response to the request.
     */
    private @Nullable DisplayTrainsResponse DisplayTrains(String query1, String query2, String query3, String query4, String query5, String query6, String query7,String query8, String sDate, String source, String dest) {
        char[] Days_Running = null;
        @MonotonicNonNull ResultSet result = null;
        int required_day = DisplayTrainsRequestHandler.DayToDate(sDate);
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query1, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1,source);
            preparedStatement.setString(2,dest);
            System.out.println(preparedStatement.toString());

            result = preparedStatement.executeQuery();
        ArrayList<String> Train_ID = new ArrayList<>(), Train_Name = new ArrayList<>(), Departure = new ArrayList<>(), Arrival = new ArrayList<>(), First_AC = new ArrayList<>(), Second_AC = new ArrayList<>(), Third_AC = new ArrayList<>(), Sleeper = new ArrayList<>();
        int i = 0;
        ArrayList<Integer>AC3Fare=new ArrayList<>(),AC2Fare=new ArrayList<>(),AC1Fare=new ArrayList<>(),SLFare=new ArrayList<>();
        while (true) {
            if (!result.next())
                break;
            @SuppressWarnings("assignment.type.incompatible") // Day_No cannot be null. Refer the README.
            @NonNull String Day_No = result.getString("Day_No"),
                    s = result.getString("Days_Running");
            Days_Running = s.toCharArray();
            char value = Days_Running[required_day - Integer.parseInt(Day_No)];
            if (value == '1') {
                try {
                    preparedStatement = connection.prepareStatement(query7);
                    preparedStatement.setString(1, result.getString("Train_ID"));
                    ResultSet resultSet1 = preparedStatement.executeQuery();
                    resultSet1.next();
                    int compare1, compare2, counter = 0;

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        java.util.Date date = sdf.parse(sDate);
                        @Nullable String addedTill = resultSet1.getString("Added_Till"),
                                cancelledTill = resultSet1.getString("Cancelled_Till");
                        if (addedTill != null) {
                            java.util.Date date1 = sdf.parse(addedTill);
                            compare1 = date.compareTo(date1);
                            if (compare1 < 0) {
                                if (cancelledTill != null) {
                                    java.util.Date date2 = sdf.parse(cancelledTill);
                                    compare2 = date.compareTo(date2);
                                    if (compare2 > 0) {
                                        counter = 1;
                                    }
                                } else counter = 1;
                            }
                        } else if (cancelledTill != null) {
                            java.util.Date date2 = sdf.parse(cancelledTill);
                            compare2 = date.compareTo(date2);
                            if (compare2 > 0) {
                                counter = 1;
                            }
                        } else counter = 1;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (counter == 1) {
                        @SuppressWarnings("assignment.type.incompatible") // Train_ID cannot be null. Refer the README.
                        @NonNull String trainID = result.getString("Train_ID"),
                                trainName = result.getString("Train_Name");
                        Train_ID.add(trainID);
                        Train_Name.add(trainName);

                        @Nullable String time = result.getString("Departure");
                        if(time != null)
                            Departure.add(time);
                        else
                            Departure.add("End");

                        time = result.getString("Arrival");
                        if(time != null)
                            Arrival.add(time);
                        else
                            Arrival.add("Start");

                        preparedStatement = connection.prepareStatement(query2);
                        preparedStatement.setString(1, Train_ID.get(i));
                        ResultSet resultSet = preparedStatement.executeQuery();

                        // Getting the ticket cost for each class
                        preparedStatement=connection.prepareStatement(query8);
                        preparedStatement.setString(1,result.getString("Train_ID"));
                        preparedStatement.setString(2,source);
                        preparedStatement.setString(3,dest);
                        ResultSet d=preparedStatement.executeQuery();
                        d.next();
                        int d1=d.getInt(1);
                        d.next();
                        int d2=d.getInt(1);
                        int distance=d2-d1;

                        preparedStatement = connection.prepareStatement(query3);
                        preparedStatement.setString(1, Train_ID.get(i));
                        preparedStatement.setString(2, Train_ID.get(i));
                        preparedStatement.setString(3, Train_ID.get(i));
                        preparedStatement.setString(4, source);
                        preparedStatement.setString(5, Train_ID.get(i));
                        preparedStatement.setString(6, dest);
                        preparedStatement.setString(7, sDate);
                        ResultSet SL_Seats = preparedStatement.executeQuery();
                        resultSet.next();
                        SL_Seats.next();
                        int available_seats = 0;
                        @Nullable String number = resultSet.getString("Sleeper_Seats");
                        if (number != null) {
                            @SuppressWarnings("assignment.type.incompatible") // If number of seats is not null,
                            // number of coaches will not be null either.
                            @NonNull String coaches = resultSet.getString("Sleeper_Coaches");

                            @Nullable String occupiedCount = SL_Seats.getString(1);
                            available_seats = Integer.parseInt(number) - Integer.parseInt(
                                    (occupiedCount == null)? "0" : occupiedCount);
                            Sleeper.add(String.valueOf(available_seats * Integer.parseInt(coaches)));
                        } else Sleeper.add("N/A");
                        if(resultSet.getString(9)!=null)
                        {
                            if(Objects.equals(result.getString("Train_ID"), "22209") &&
                                    resultSet.getString("FirstAC_Seats") != null)//dynamic pricing for garib rath train
                            {
                                @Nullable String coaches = resultSet.getString("Sleeper_Coaches");

                                @Nullable String occupiedCount = SL_Seats.getString(1);
                                float percentageBooked=Float.parseFloat((occupiedCount != null)? occupiedCount :
                                        "0")/(Float.parseFloat(number != null ? number : "9999999") *
                                        Float.parseFloat(coaches != null ? coaches : "9999999"));
                                SLFare.add((int) (resultSet.getInt(9)*distance*(1+percentageBooked)));
                            }
                            SLFare.add(resultSet.getInt(9)*distance);
                        }
                        else SLFare.add(-1);

                        preparedStatement = connection.prepareStatement(query4);
                        preparedStatement.setString(1, Train_ID.get(i));
                        preparedStatement.setString(2, Train_ID.get(i));
                        preparedStatement.setString(3, Train_ID.get(i));
                        preparedStatement.setString(4, source);
                        preparedStatement.setString(5, Train_ID.get(i));
                        preparedStatement.setString(6, dest);
                        preparedStatement.setString(7, sDate);
                        ResultSet AC1_Seats = preparedStatement.executeQuery();
                        AC1_Seats.next();
                        number = resultSet.getString("FirstAC_Seats");
                        if (number != null) {
                            @SuppressWarnings("assignment.type.incompatible") // If number of seats is not null,
                            // number of coaches will be non null too.
                            @NonNull String coach = resultSet.getString("FirstAC_Coaches");
                            @Nullable String occupiedCount = AC1_Seats.getString(1);
                            available_seats = Integer.parseInt(number) - Integer.parseInt(occupiedCount != null ? occupiedCount : "0");
                            First_AC.add(String.valueOf(available_seats * Integer.parseInt(coach)));
                        } else First_AC.add("N/A");
                        if(resultSet.getString(10)!=null)
                        {
                            if(Objects.equals(result.getString("Train_ID"), "22209") &&resultSet.getString("FirstAC_Seats") != null)//dynamic pricing for garib rath train
                            {
                                @Nullable String occupiedCount = AC1_Seats.getString(1);
                                @Nullable String coaches = resultSet.getString("FirstAC_Coaches");
                                float percentageBooked=Float.parseFloat(occupiedCount != null ? occupiedCount : "0")/
                                        (Float.parseFloat(number != null ? number : "99999")*Float.parseFloat(coaches != null ? coaches : "99999"));
                                AC1Fare.add((int) (resultSet.getInt(10)*distance*(1+percentageBooked)));
                            }
                            else AC1Fare.add(resultSet.getInt(10)*distance);
                        }
                        else AC1Fare.add(-1);

                        preparedStatement = connection.prepareStatement(query5);
                        preparedStatement.setString(1, Train_ID.get(i));
                        preparedStatement.setString(2, Train_ID.get(i));
                        preparedStatement.setString(3, Train_ID.get(i));
                        preparedStatement.setString(4, source);
                        preparedStatement.setString(5, Train_ID.get(i));
                        preparedStatement.setString(6, dest);
                        preparedStatement.setString(7, sDate);
                        ResultSet AC2_Seats = preparedStatement.executeQuery();
                        AC2_Seats.next();
                        number = resultSet.getString("SecondAC_Seats");
                        if (number != null) {
                            @SuppressWarnings("assignment.type.incompatible") // If number of seats is not null, number
                            // of coaches will not be null either.
                            @NonNull String coach = resultSet.getString("SecondAC_Coaches");
                            @Nullable String occupiedCount = AC2_Seats.getString(1);
                            available_seats = Integer.parseInt(number) - Integer.parseInt(occupiedCount != null ? occupiedCount : "0");
                            Second_AC.add(String.valueOf(available_seats * Integer.parseInt(coach)));
                        } else Second_AC.add("N/A");
                        if(resultSet.getString(11)!=null)
                        {
                            if(Objects.equals(result.getString("Train_ID"), "22209") &&resultSet.getString("SecondAC_Seats") != null)//dynamic pricing for garib rath train
                            {
                                @Nullable String coach = resultSet.getString("SecondAC_Coaches");
                                @Nullable String occupiedCount = AC2_Seats.getString(1);
                                float percentageBooked=Float.parseFloat(occupiedCount != null ? occupiedCount : "0")
                                        /(Float.parseFloat(number != null ? number : "99999")*Float.parseFloat(coach != null ? coach : "99999"));
                                AC2Fare.add((int) (resultSet.getInt(11)*distance*(1+percentageBooked)));
                            }
                            else AC2Fare.add(resultSet.getInt(11)*distance);
                        }
                        else AC2Fare.add(-1);

                        preparedStatement = connection.prepareStatement(query6);
                        preparedStatement.setString(1, Train_ID.get(i));
                        preparedStatement.setString(2, Train_ID.get(i));
                        preparedStatement.setString(3, Train_ID.get(i));
                        preparedStatement.setString(4, source);
                        preparedStatement.setString(5, Train_ID.get(i));
                        preparedStatement.setString(6, dest);
                        preparedStatement.setString(7, sDate);
                        ResultSet AC3_Seats = preparedStatement.executeQuery();
                        AC3_Seats.next();
                        number = resultSet.getString("ThirdAC_Seats");
                        if (number != null) {
                            @SuppressWarnings("assignment.type.incompatible") // If number of seats is not null, number
                            // of coaches will not be null either.
                            @NonNull String coach = resultSet.getString("ThirdAC_Coaches");
                            @Nullable String occupiedCount = AC3_Seats.getString(1);
                            available_seats = Integer.parseInt(number) - Integer.parseInt(occupiedCount != null ? occupiedCount : "0");
                            Third_AC.add(String.valueOf(available_seats * Integer.parseInt(coach)));
                        } else Third_AC.add("N/A");
                        if(resultSet.getString(12)!=null)
                        {
                            if(Objects.equals(result.getString("Train_ID"), "22209") &&resultSet.getString("ThirdAC_Seats") != null)//dynamic pricing for garib rath train
                            {
                                @Nullable String coach = resultSet.getString("ThirdAC_Coaches");
                                @Nullable String occupiedCount = AC3_Seats.getString(1);
                                float percentageBooked=Float.parseFloat(occupiedCount != null ? occupiedCount :
                                        "0")/(Float.parseFloat(number != null ? number : "99999")*
                                        Float.parseFloat(coach != null ? coach : "99999"));
                                AC3Fare.add((int) (resultSet.getInt(12)*distance*(1+percentageBooked)));
                            }
                            else AC3Fare.add(resultSet.getInt(12)*distance);
                        }
                        else AC3Fare.add(-1);
                        i++;

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return new DisplayTrainsResponse(Train_ID,Train_Name,Departure,Arrival,First_AC,Second_AC,Third_AC,Sleeper,sDate,source,dest,AC1Fare,AC2Fare,AC3Fare,SLFare);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}