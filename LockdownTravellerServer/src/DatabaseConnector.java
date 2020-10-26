import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseConnector<ObjectOutputStream> {
    private Connection connection = null;

    public DatabaseConnector() {
        connection = Server.getConnection();
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




//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//         return new DisplayTrainsResponse(Train_ID,Train_Name,Source,Departure,Destination,Arrival,First_AC,Second_AC,Third_AC,Sleeper,Date,i);
//
//    }
    public CancelBookingResponse CancelBooking(String query, ObjectOutputStream oos) throws SQLException, IOException {
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        int result=preparedStatement.executeUpdate();
        String response = null;
        if(result==0){response="No bookings found under the given PNR.Failed to cancel the booking";}
        else if(result>0){response="Booking cancelled succesfully";}
        return new CancelBookingResponse(response);
    }



    public Connection getConnection() {
        return connection;
    }

}
