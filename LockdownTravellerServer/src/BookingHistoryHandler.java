import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class BookingHistoryHandler {
    Connection connection;
    ObjectOutputStream outputStream;
    BookingHistoryRequest historyRequest;

    public BookingHistoryHandler(Connection connection, ObjectOutputStream outputStream, BookingHistoryRequest historyRequest){
        this.connection=connection;
        this.outputStream=outputStream;
        this.historyRequest=historyRequest;
    }
    public void sendQuery() {
        String query1 = "select distinct PNR from Booking_Info where User_ID = ?;";
        String query2 = "select * from Booking_Info where PNR = ?;";
        String query3 = "select * from Vacancy_Info where (Station_No = (select max(Station_No) from Vacancy_Info where " +
                "Booking_ID = ?) or Station_No = (select min(Station_No) from Vacancy_Info where Booking_ID = ?)) and " +
                "Booking_ID = ? order by Station_No;";
        BookingHistoryResponse historyResponse = bookinghistory(historyRequest,query1, query2, query3);
        Server.SendResponse(outputStream,historyResponse);
    }



public BookingHistoryResponse bookinghistory(BookingHistoryRequest historyRequest, String query1, String query2, String query3) {
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
            pnr.add(resultSet.getString("PNR"));
        }
        final int n = pnr.size();
        for(int i = 0; i < n; i++) {
            System.out.println(pnr.get(i));
            PreparedStatement perPnrQuery = connection.prepareStatement(query2);
            String confirmedBookingID = "";
            perPnrQuery.setString(1, pnr.get(i));
            ResultSet bookingForPnr = perPnrQuery.executeQuery();
            bookingID.add(new ArrayList<>());
            name.add(new ArrayList<>());
            age.add(new ArrayList<>());
            gender.add(new ArrayList<>());
            while(bookingForPnr.next()) {
                bookingID.get(i).add(bookingForPnr.getString("Booking_ID"));
                if(bookingForPnr.getString("Booking_Status").equals("Confirmed"))
                    confirmedBookingID = bookingForPnr.getString("Booking_ID");
                name.get(i).add(bookingForPnr.getString("Passenger_Name"));
                age.get(i).add(bookingForPnr.getInt("Passenger_Age"));
                gender.get(i).add(bookingForPnr.getString("Passenger_Gender"));
            }
            PreparedStatement otherDetails = connection.prepareStatement(query3);
            if(!confirmedBookingID.equals("")) {
                otherDetails.setString(1, confirmedBookingID);
                otherDetails.setString(2, confirmedBookingID);
                otherDetails.setString(3, confirmedBookingID);
                System.out.println(otherDetails.toString());
                ResultSet others = otherDetails.executeQuery();
                others.next();
                source.add(others.getString("Station"));
                date.add(others.getString("Date"));
                others.next();
                destination.add("Station");
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
