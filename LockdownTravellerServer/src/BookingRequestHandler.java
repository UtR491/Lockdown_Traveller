import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class BookingRequestHandler extends Handler{
    DatabaseConnector db = null;
     BookingRequest bookingRequest = null;
    ObjectOutputStream oos = null;
    public BookingRequestHandler(DatabaseConnector db, BookingRequest bookingRequest, ObjectOutputStream oos) {
            this.bookingRequest = bookingRequest;
            this.db = db;
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

            // Find seats which match the criteria and cannot be booked.
            String query1 = "select distinct Seat_No from Vacancy_Info where Train_ID = '" + trainId + "'"
                    + " and Seat_No like '" + coach + "%' and Date = '"+convertedDate+"'";
            String query2 = "select * from Route_Info where Train_ID = '" + trainId + "'"
                    + "and Distance_Covered between"
                    + "(select Distance_Covered from Route_Info where Train_ID = '" + trainId + "' and Station =  '" + source +"')"
                    + "and"
                    + "(select Distance_Covered from Route_Info where Train_ID = '" + trainId + "' and Station =  '" + destination +"')";
            String coachColumn =null;
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
            BookingResponse br =  db.bookingRequest(query1, query2, query3, query4, numSeat, availableSeat, query5, bookingIds, preference, age, gender);
            BookingResponse.SendResponse(oos,br);



            System.out.println(query1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
