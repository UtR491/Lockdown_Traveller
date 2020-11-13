import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class has a bit of an ambiguous name. The response object contains all the bookings, previous as well as the upcoming
 * ones.
 */
public class BookingHistoryRequestHandler extends Handler {

    final private Connection connection;
    final private ObjectOutputStream outputStream;
    final private BookingHistoryRequest historyRequest;

    /**
     * Constructor. Initializes the object with the database connection, request object and output stream to send the
     * response.
     * @param connection The connection to the database to execute updates and queries.
     * @param historyRequest The booking history request object with relevant information.
     * @param outputStream The output stream to send the object.
     */
    public BookingHistoryRequestHandler(Connection connection, ObjectOutputStream outputStream, BookingHistoryRequest historyRequest){
        this.connection=connection;
        this.outputStream=outputStream;
        this.historyRequest=historyRequest;
    }

    /**
     * This is the first function that is called inside the request identifier. In this function, we form the relevant
     * sql queries and pass it to another function to execute.
     */
    @Override
    public void sendQuery() {
        // Find all the PNRs in the database that store the information about bookings made by a user with some
        // particular id.
        String query1 = "select distinct PNR from Booking_Info where User_ID = ?;";
        // Select data with the same PNR. So these can be stored and displayed as a unit.
        String query2 = "select * from Booking_Info where PNR = ?;";
        // Get the source and destination from any confirmed seat with a booking id that matches the PNR.
        String query3 = "select * from Vacancy_Info where (Station_No = (select max(Station_No) from Vacancy_Info where " +
                "Booking_ID = ?) or Station_No = (select min(Station_No) from Vacancy_Info where Booking_ID = ?)) and " +
                "Booking_ID = ? order by Station_No;";
        BookingHistoryResponse historyResponse = bookinghistory(historyRequest,query1, query2, query3);
        Server.SendResponse(outputStream,historyResponse);
    }

    /**
     * This is where the statements are executed.
     * @param historyRequest History request object.
     * @params Queries.
     * @return Response to the Booking history request.
     */
    public @Nullable BookingHistoryResponse bookinghistory(BookingHistoryRequest historyRequest, String query1, String query2, String query3) {

        // All fields that will be a part of the response object.
        ArrayList<ArrayList<String>> bookingID = new ArrayList<>();
        ArrayList<ArrayList<String>> name = new ArrayList<>();
        ArrayList<ArrayList<Integer>> age = new ArrayList<>();
        ArrayList<ArrayList<String>> gender = new ArrayList<>();
        ArrayList<String> pnr = new ArrayList<>();
        ArrayList<String> source = new ArrayList<>();
        ArrayList<String> destination = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();

        ResultSet resultSet;
        try {
            PreparedStatement getPnr = connection.prepareStatement(query1);
            getPnr.setString(1, historyRequest.getUserid());
            resultSet = getPnr.executeQuery();
            while(resultSet.next()) {

                @SuppressWarnings("assignment.type.incompatible") // PNR cannot be null. Refer the README.
                @NonNull String s = resultSet.getString("PNR");
                pnr.add(s);
            }
            final int n = pnr.size();
            for(int i = 0; i < n; i++) {
                System.out.println(pnr.get(i));
                PreparedStatement perPnrQuery = connection.prepareStatement(query2);
                @MonotonicNonNull String confirmedBookingID = null;
                perPnrQuery.setString(1, pnr.get(i));
                ResultSet bookingForPnr = perPnrQuery.executeQuery();
                bookingID.add(new ArrayList<>());
                name.add(new ArrayList<>());
                age.add(new ArrayList<>());
                gender.add(new ArrayList<>());
                while(bookingForPnr.next()) {
                    @SuppressWarnings("assignment.type.incompatible") // Booking_ID cannot be null. Refer the README.
                    @NonNull String s = bookingForPnr.getString("Booking_ID");
                    bookingID.get(i).add(s);

                    @SuppressWarnings("assignment.type.incompatible") // Booking_Status cannot be null. Refer the README.
                    @NonNull String bookingStatus = bookingForPnr.getString("Booking_Status");
                    if(bookingStatus.equals("Confirmed")) {
                        @SuppressWarnings("assignment.type.incompatible") // Booking ID cannot be null. Refer the README.
                        @NonNull String confirmedId = bookingForPnr.getString("Booking_ID");
                        confirmedBookingID = confirmedId;
                    }

                    @SuppressWarnings("assignment.type.incompatible") // Passenger_Name cannot be null. Refer the README.
                    @NonNull String pName = bookingForPnr.getString("Passenger_Name");
                    name.get(i).add(pName);

                    age.get(i).add(bookingForPnr.getInt("Passenger_Age"));

                    @SuppressWarnings("assignment.type.incompatible") // Gender cannot be null. Refer the README.
                    @NonNull String pGender = bookingForPnr.getString("Passenger_Gender");
                    gender.get(i).add(pGender);
                }
                PreparedStatement otherDetails = connection.prepareStatement(query3);
                if(confirmedBookingID != null) {
                    otherDetails.setString(1, confirmedBookingID);
                    otherDetails.setString(2, confirmedBookingID);
                    otherDetails.setString(3, confirmedBookingID);
                    System.out.println(otherDetails.toString());
                    ResultSet others = otherDetails.executeQuery();
                    others.next();
                    System.out.println(query3);
                    @SuppressWarnings("assignment.type.incompatible") // Station cannot be null. Refer the README.
                    @NonNull String s = others.getString("Station");
                    source.add(s);

                    @SuppressWarnings("assignment.type.incompatible") // Date cannot be null. Refer the README.
                    @NonNull String pDate = others.getString("Date");
                    date.add(pDate);

                    others.next();

                    @SuppressWarnings("assignment.type.incompatible") // Station cannot be null. Refer the README.
                    @NonNull String pDestination = others.getString("Station");
                    destination.add(pDestination);
                } else {
                    source.add("N/A");
                    destination.add("N/A");
                    date.add("N/A");
                }
            }
            return new BookingHistoryResponse(bookingID, name, age, gender, pnr, date, source, destination,
                    historyRequest.getUserid());
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
