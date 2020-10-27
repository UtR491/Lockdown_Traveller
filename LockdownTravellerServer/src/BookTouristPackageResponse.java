import java.io.Serializable;
import java.util.ArrayList;

public class BookTouristPackageResponse extends Response implements Serializable {
    ArrayList<String>bookingID,name;
    ArrayList<Integer>allottedSeatNo;
    int allottedCoachNo;
    String error;

    public BookTouristPackageResponse(String error) {
        this.error = error;
    }

    public BookTouristPackageResponse(ArrayList<String> bookingID, ArrayList<String> name, ArrayList<Integer> allottedSeatNo, int allottedCoachNo) {
        this.bookingID = bookingID;
        this.name = name;
        this.allottedSeatNo = allottedSeatNo;
        this.allottedCoachNo=allottedCoachNo;
    }

    public String getError() {
        return error;
    }

    public int getAllottedCoachNo() {
        return allottedCoachNo;
    }

    public ArrayList<Integer> getAllottedSeatNo() {
        return allottedSeatNo;
    }

    public ArrayList<String> getBookingID() {
        return bookingID;
    }

    public ArrayList<String> getName() {
        return name;
    }
}
