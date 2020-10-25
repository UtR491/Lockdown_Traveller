import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;


public class BookingRequestHandler extends Handler{
     BookingRequest bookingRequest;
     Connection connection;
    ObjectOutputStream oos;
    public BookingRequestHandler(Connection connection, BookingRequest bookingRequest, ObjectOutputStream oos) {
            this.bookingRequest = bookingRequest;
            this.connection = connection;
            this.oos = oos;
    }

@Override
    public void sendQuery() {
        try {

            System.out.println("inside the gottaDo function");
            String trainId = bookingRequest.getTrainId();
            Date date = bookingRequest.getDate();
            String source = bookingRequest.getSource();
            String destination = bookingRequest.getDestination();
            String coach = bookingRequest.getCoach();
            String[] preference = bookingRequest.getPreference();
            int numSeat = bookingRequest.getNumSeat();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String convertedDate = df.format(date);
            String userId = bookingRequest.getUserId();
            int availableSeat = bookingRequest.getAvailableSeat();

            String[] name = bookingRequest.getName();
            int[] age = bookingRequest.getAge();
            char[] gender = bookingRequest.getGender();

            long[] bookingIds = new long[numSeat];

            String query1 = "select distinct Seat_No from " + "Vacancy_Info where" + " Train_ID = ?  and Seat_No like ? and Date = ?";
            String query2 = "select * from Route_Info where " +
                    "Train_ID = ? and Distance_Covered between" + "(select Distance_Covered from Route_Info " +
                    "where Train_ID = ? and Station = ?)"
                    + "and"
                    + "(select Distance_Covered from Route_Info where Train_ID = '" + trainId + "' and Station =  '" + destination +"')";

                    String coachColumn =null;

            // Find seats which match the criteria and cannot be booked.

//            String query1 = "select distinct Seat_No from Vacancy_Info where Train_ID = '" + trainId + "'"
//                    + " and Seat_No like '" + coach + "%' and Date = '"+convertedDate+"'";
//            String query2 = "select * from Route_Info where Train_ID = '" + trainId + "'"
//                    + "and Distance_Covered between"
//                    + "(select Distance_Covered from Route_Info where Train_ID = '" + trainId + "' and Station =  '" + source +"')"
//                    + "and"
//                    + "(select Distance_Covered from Route_Info where Train_ID = '" + trainId + "' and Station =  '" + destination +"')";

            System.out.println(coach);
            if(coach.equals("SL"))
            {
                coachColumn = "Sleeper_Seats";
            }
            String query3 = "select " + coachColumn + " from Basic_Train_Info where Train_ID = '" + trainId + "';";
            long min = (long) 1e9, max = (long) 1e10;
            long bookingId = ThreadLocalRandom.current().nextLong(min, max);
            bookingIds[0] = bookingId;

            String query4 = "insert into Booking_Info (Booking_ID, PNR, User_ID, Passenger_Name, Passenger_Age, Passenger_Gender, Booking_Status) "
                    + "values ('" + bookingId + "', '" + "pnr" + "', '" + userId + "', '" + name[0] + "', '" + age[0] + "', '" + gender[0] + "', 'confirmed')";

            for(int i=1;i<numSeat;i++) {
                bookingId = ThreadLocalRandom.current().nextLong(min, max);
                bookingIds[i]=bookingId;
                query4 = query4 + ", ('" + bookingId + "', '" + "pnr" + "', '" + userId + "', '" + name[i] + "', '" + age[i] + "', '" + gender[i] + "', 'confirmed')";
            }
            query4+=';';
            System.out.println(query4);
            String query5="insert into Vacancy_Info (Train_ID, Booking_ID, Date, Station, Station_No, Seat_No) "
                    + "values ('" + trainId + "', 'xxxxxxxxxx', '" + convertedDate + "', 'station', 'stationNo', '" + coach + "xx')";
            System.out.println("getting the booking response object from calling bookingRequest function in db connector");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
