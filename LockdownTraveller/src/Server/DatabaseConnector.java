package Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseConnector {
    private Connection connection = null;

    public DatabaseConnector() {
        getConnection();
    }

    public BookingResponse bookingRequest(String query1, String query2, String query3, String query4,
                                          int numSeat, int availableSeat, String query5, long[] bookingIds,
                                          String[] preference, int[] age, char[] gender) {
        ResultSet conflict;
        ResultSet stationsOnRoute;
        ResultSet seatInCoach;
        String inClause = "(";
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
                inClause += "'" + stationsOnRoute.getString("Station") + "', ";
                route.add(String.valueOf(stationsOnRoute.getString("Station")));
            }
            inClause = inClause + "'')";

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

    public void getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties properties = new Properties();
            properties.put("user", "utkarsh");
            properties.put("password", "Hello@123");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/lockdown_traveller", properties);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
