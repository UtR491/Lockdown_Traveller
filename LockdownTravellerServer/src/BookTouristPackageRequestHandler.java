

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookTouristPackageRequestHandler extends Handler {
    Connection connection;
    ObjectOutputStream oos;
    BookTouristPackageRequest bookTouristPackageRequest;

    public BookTouristPackageRequestHandler(Connection connection, ObjectOutputStream oos, BookTouristPackageRequest bookTouristPackageRequest) {
        this.connection = connection;
        this.oos = oos;
        this.bookTouristPackageRequest = bookTouristPackageRequest;
    }

    @Override
    void sendQuery() {
        String query1="select distinct Coach_No from Package_Booking where Package_Code=?";
        String query2="select count(Seat_No) from Package_Booking where Package_Code=? and Coach_No=in (select distinct Coach_No from Package_Booking where Package_Code=?) ;";
        String query3="select MAX(Seat_No) from Package_Booking where Package_Code=? and Coach_No=?;";
        String query4="insert into Package_Booking values(?,?,?,?,?,?,?,?,?,?);";
        BookTouristPackageResponse bookTouristPackageResponse=bookTouristPackage(query1,query2,query3,query4,bookTouristPackageRequest);
        Server.SendResponse(oos,bookTouristPackageResponse);

    }
    public BookTouristPackageResponse bookTouristPackage(String query1,String query2,String query3,String query4,BookTouristPackageRequest bookTouristPackageRequest)
    {
        ArrayList<Integer>coach=null,seatVacant = null;
        ArrayList<String>BookingID = null,name=null;
        ArrayList<Integer>allottedSeatNo = null;
        int allottedCoachNo=0;
        PreparedStatement preparedStatement;
        String error,bookingID=null;
        try {
            preparedStatement = connection.prepareStatement(query1);
            preparedStatement.setString(1,bookTouristPackageRequest.getPackageID());
            ResultSet currentCoach=preparedStatement.executeQuery();
            while(currentCoach.next());
            {
                assert false;
                coach.add(currentCoach.getInt(1));}
            preparedStatement=connection.prepareStatement(query2);
            preparedStatement.setString(1,bookTouristPackageRequest.getPackageID());
            preparedStatement.setString(2,bookTouristPackageRequest.getPackageID());
            ResultSet currentSeat=preparedStatement.executeQuery();
            while(currentSeat.next());
            {
                assert false;
                seatVacant.add(24-currentSeat.getInt(1));}

            for(int i=0;i<coach.size();i++)
            {
                if(seatVacant.get(i)>=bookTouristPackageRequest.getAge().size())
                {
                    preparedStatement=connection.prepareStatement(query3);
                    preparedStatement.setString(1,bookTouristPackageRequest.getPackageID());
                    preparedStatement.setInt(2, coach.get(i));
                    ResultSet resultSet=preparedStatement.executeQuery();
                    resultSet.next();
                    int currSeat=resultSet.getInt(1)+1;

                    for (int j=0;j<bookTouristPackageRequest.getAge().size();j++)
                    {
                        preparedStatement=connection.prepareStatement(query4);
                        preparedStatement.setString(1,bookTouristPackageRequest.getPackageID());
                        preparedStatement.setString(2,bookingID);
                        preparedStatement.setString(3,bookTouristPackageRequest.getUserID());
                        try {
                            preparedStatement.setString(4,bookTouristPackageRequest.getBoardingPoint());
                            preparedStatement.setString(5,bookTouristPackageRequest.getName().get(j));
                            preparedStatement.setInt(6,bookTouristPackageRequest.getAge().get(j));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        preparedStatement.setString(7,bookTouristPackageRequest.getGender().get(j).toString());
                        preparedStatement.setInt(8,coach.get(i));
                        preparedStatement.setInt(9,currSeat);
                        preparedStatement.setString(10,bookTouristPackageRequest.getMealPreference().get(j));
                        int c=preparedStatement.executeUpdate();
                        if(c==0){error="Error Occured";return new BookTouristPackageResponse(error);}
                        allottedCoachNo=coach.get(i);
                        assert false;
                        allottedSeatNo.add(currSeat);
                        assert false;
                        BookingID.add(bookingID);
                        name.add(bookTouristPackageRequest.getName().get(j));
                        currSeat+=1;

                    }
                    break;
                }
                else if(coach.get(i)==5)
                {
                    error="No vacancy";
                    return new BookTouristPackageResponse(error);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new BookTouristPackageResponse(BookingID,name,allottedSeatNo,allottedCoachNo);
    }
}
