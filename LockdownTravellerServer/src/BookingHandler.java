import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingHandler {


    String Train_Name;
    String time;
    int PNR;
    int BookindID;
    int seatno; int AC3uppercount = 3, AC3middlecount = 2, AC3lowercount = 1;
    int SLuppercount = 3, SLmiddlecount = 2, SLlowercount = 1;
    int AC2uppercount = 2, AC2lowercount = 1;
    int AC1lower = 1;


    public void BookingHandler(int AC3uppercount, int AC3middlecount , int AC3lowercount, int SLuppercount,
                               int SLmiddlecount, int SLlowercount, int AC2uppercount, int AC2lowercount, int AC1lower ){
        this.AC3uppercount=AC3uppercount;
        AC3uppercount=3;
        this.AC3middlecount=AC3middlecount;
        AC3middlecount=2;
        this.AC3lowercount=AC3lowercount;
        AC3lowercount=1;


    }

    public void allotSeatno() {
        Server.getConnection();
        BookingRequest bookingRequest = null;
        String trainId = bookingRequest.getTrainId();
        Date date = bookingRequest.getDate();
        String source = bookingRequest.getSource();
        String destination = bookingRequest.getDestination();
        String coach = bookingRequest.getCoach();
//        String[] preference = bookingRequest.getPreference();
        int numSeat = bookingRequest.getNumSeat();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String convertedDate = df.format(date);
        String userId = bookingRequest.getUserId();
        int availableSeat = bookingRequest.getAvailableSeat();
        int[] age =bookingRequest.getAge();
        String[] name = bookingRequest.getName();
        char[] gender = bookingRequest.getGender();
        long bookingIds = 0; // booking ID code

    //
        int i, n, j;
        n=bookingRequest.getNumSeat();
        for (i = 1; i <= 8; i++) {
            for (j=0;j<n;j++){
                try {
                    PreparedStatement preparedStatement = Server.getConnection().prepareStatement("select Station from Route_Info where " +
                            "Train_Id =? and between source = ? and destination =? order by Station_no");
                    preparedStatement.setString(1, trainId);
                    preparedStatement.setString(2, source);
                    preparedStatement.setString(3, destination);
                } catch (SQLException e){
                    e.printStackTrace();
                }

                if(10<=age[j] || age[j]<30) {
                    i++;
                    seatno=3*i;
                    try {
                        PreparedStatement allot_upperBerth = Server.getConnection().prepareStatement(" update Vacancy_Info set Seat_No= ? " +
                                "where  + Train_ID=? and Booking_ID= ? and Date=?");
                        allot_upperBerth.setInt(1, seatno);
                        allot_upperBerth.setString(2, trainId);
                        allot_upperBerth.setLong(3, bookingIds);
                        allot_upperBerth.setString(4, convertedDate);
//                        coach to be added in the tables
                    }catch (SQLException e){
                        e.printStackTrace();
                    }

                }
                else if (30<=age[j] || age[j] <45){
                    i++;
                    seatno=2*i;
                    try {
                        PreparedStatement allot_upperBerth = Server.getConnection().prepareStatement(" update Vacancy_Info set Seat_No= ? " +
                                "where  + Train_ID=? and Booking_ID= ? and Date=?");
                        allot_upperBerth.setInt(1, seatno);
                        allot_upperBerth.setString(2, trainId);
                        allot_upperBerth.setLong(3, bookingIds);
                        allot_upperBerth.setString(4, convertedDate);
//                        coach to be added in the tables
                    }catch (SQLException e){
                        e.printStackTrace();
                    }

                }
                else {
                    i++;
                    seatno=i;
                    try {
                        PreparedStatement allot_upperBerth = Server.getConnection().prepareStatement(" update Vacancy_Info set Seat_No= ? " +
                                "where  + Train_ID=? and Booking_ID= ? and Date=?");
                        allot_upperBerth.setInt(1, seatno);
                        allot_upperBerth.setString(2, trainId);
                        allot_upperBerth.setLong(3, bookingIds);
                        allot_upperBerth.setString(4, convertedDate);
//                        coach to be added in the tables
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }

            }


        }

    }
}






