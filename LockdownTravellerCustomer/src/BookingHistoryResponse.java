import java.io.Serializable;
import java.util.ArrayList;

public class BookingHistoryResponse extends Response implements Serializable {
    final private ArrayList<ArrayList<String>> bookingID;
    final private ArrayList<ArrayList<String>> name;
    final private ArrayList<ArrayList<Integer>> age;
    final private ArrayList<ArrayList<String>> gender;
    final private ArrayList<String> pnr;
    final private ArrayList<String> date;
    final private ArrayList<String> source;
    final private ArrayList<String> destination;
    final private String userID;

    public BookingHistoryResponse(ArrayList<ArrayList<String>> bookingID, ArrayList<ArrayList<String>> name, ArrayList<ArrayList<Integer>> age, ArrayList<ArrayList<String>> gender, ArrayList<String> pnr, ArrayList<String> date, ArrayList<String> source, ArrayList<String> destination, String userID) {
        this.bookingID = bookingID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.pnr = pnr;
        this.date = date;
        this.source = source;
        this.destination = destination;
        this.userID = userID;
    }

    public ArrayList<ArrayList<String>> getBookingID() {
        return bookingID;
    }

    public ArrayList<ArrayList<String>> getName() {
        return name;
    }

    public ArrayList<ArrayList<Integer>> getAge() {
        return age;
    }

    public ArrayList<ArrayList<String>> getGender() {
        return gender;
    }

    public ArrayList<String> getPnr() {
        return pnr;
    }

    public String getUserid() {
        return userID;
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public ArrayList<String> getSource() {
        return source;
    }

    public ArrayList<String> getDestination() {
        return destination;
    }

    public String getUserID() {
        return userID;
    }
}
