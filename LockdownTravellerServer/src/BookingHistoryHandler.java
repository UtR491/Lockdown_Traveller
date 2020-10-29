

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
    public void sendQuery() throws SQLException {

        String query= "select * from Booking_Info, Vacancy_Info where Booking_Info.Booking_ID=? and Booking_Info.Passenger_Name =?," +
                " and Booking_Info.User_ID and Booking_Info.Passenger_Age=? and Booking_Info.Passenger_Gender=? and " +
                "Vacancy_Info.Booking_ID and Vacancy_Info.Date and ";
        BookingHistoryResponse historyResponse = bookinghistory(historyRequest,query);
        Server.SendResponse(outputStream,historyResponse);

    }



public BookingHistoryResponse bookinghistory(BookingHistoryRequest historyRequest, String query) throws SQLException {
    ArrayList<String> bookingID = null;
    ArrayList<String> name = null;
    ArrayList<Integer> age = null;
    ArrayList<String> gender=null;
    String pnr=null;
    ResultSet resultSet=null;


    try {
        PreparedStatement preparedStatement = Server.getConnection().prepareStatement(query);
        preparedStatement.executeQuery();
     resultSet= preparedStatement.getResultSet();
        while (resultSet.next()){
            bookingID.add(resultSet.getString(1));
            name.add(resultSet.getString(2));
            age.add(resultSet.getInt(3));
            gender.add(resultSet.getString(4));
            System.out.println(resultSet);
        }
    }
    catch (SQLException e) {
        e.printStackTrace();
    }
    return new BookingHistoryResponse(bookingID,name,age,gender,resultSet.getString(2),
            resultSet.getString(3));

    }

}
