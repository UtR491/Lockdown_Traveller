import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CancelBookingRequestHandler extends Handler
{
    Connection connection;
    CancelBookingRequest cb;
    ObjectOutputStream oos;
    CancelBookingRequestHandler(Connection connection,CancelBookingRequest cb,ObjectOutputStream oos)
    {
        this.connection=connection;
        this.cb=cb;
        this.oos=oos;
    }

    @Override
    void sendQuery() throws  SQLException {
        System.out.println("Inside Handler's get response method");
        String PNR=cb.getPNR();
        String userID = cb.getUserId();
        String query="updateimport java.io.Serializable;
import java.time.LocalDate;

public class BookingRequest extends Request implements Serializable {
    final private String source;
    final private String trainId;
    final private LocalDate date;
    final private String destination;
    final private String coach;
    final private String userId;
    final private int availableSeat, totalCost;
    final private String[] preference;
    final private String[] name;
    final private String[] quota;
    final private int[] age;
    final private int numSeat;
    final private char[] gender;
    final private int[] meal;

    public BookingRequest(String source, String destination, String trainId, String coach, LocalDate date, String[] name,
                          int[] age, char[] gender, String userId, int availableSeat, String[] preference, String[] quota,
                          int[] meal, int numSeat, int totalCost) {
        this.source = source;
        this.destination = destination;
        this.trainId = trainId;
        this.coach = coach;
        this.preference = preference;
        this.date = date;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.quota = quota;
        this.meal = meal;
        this.userId = userId;
        this.availableSeat = availableSeat;
        this.numSeat = numSeat;
        this.totalCost = totalCost;
    }

    public String getTrainId() {
        return trainId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String[] getPreference() {
        return preference;
    }

    public String getCoach() {
        return coach;
    }

    public String[] getName() { return name; }

    public int[] getAge() { return age; }

    public char[] getGender() { return gender; }

    public String getUserId() { return userId; }

    public int getAvailableSeat() { return availableSeat; }

    public int getNumSeat() {
        return numSeat;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public String[] getQuota() {
        return quota;
    }

    public int[] getMeal() {
        return meal;
    }
} Booking_Info set Booking_Status='Cancelled' where PNR='"+PNR+"' and User_ID = '" + userID
                +"' and not Booking_Status = 'Cancelled';";
        CancelBookingResponse cancelBookingResponse = CancelBooking(query);
        Server.SendResponse(oos,cancelBookingResponse);
    }

    public CancelBookingResponse CancelBooking(String query) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        int result=preparedStatement.executeUpdate();
        String response = null;
        if(result==0){response="failure";}
        else if(result>0){response="success";}
        return new CancelBookingResponse(response);
    }
}
