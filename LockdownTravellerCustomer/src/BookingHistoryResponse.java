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

    /**
     * Constructor to initialize the response object.
     * @param bookingID Array of array of booking ids where each array corresponds to a PNR.
     * @param name Array of array of names.
     * @param age Array of array of age.
     * @param gender Array of array of gender.
     * @param pnr Array of PNR.
     * @param date Array of dates.
     * @param source Array of sources.
     * @param destination Array of destinations.
     * @param userID Identifier for the user.
     */
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

    /**
     * Getter for booking id.
     * @return Booking ids.
     */
    public ArrayList<ArrayList<String>> getBookingID() {
        return bookingID;
    }

    /**
     * Getter for name.
     * @return Names.
     */
    public ArrayList<ArrayList<String>> getName() {
        return name;
    }

    /**
     * Getter for ages.
     * @return Ages.
     */
    public ArrayList<ArrayList<Integer>> getAge() {
        return age;
    }

    /**
     * Getter for gender.
     * @return Gender.
     */
    public ArrayList<ArrayList<String>> getGender() {
        return gender;
    }

    /**
     * Getter for PNR.
     * @return PNRs.
     */
    public ArrayList<String> getPnr() {
        return pnr;
    }

    /**
     * Getter for user id.
     * @return User id.
     */
    public String getUserid() {
        return userID;
    }

    /**
     * Getter for dates.
     * @return Date.
     */
    public ArrayList<String> getDate() {
        return date;
    }

    /**
     * Getter of sources.
     * @return Source.
     */
    public ArrayList<String> getSource() {
        return source;
    }

    /**
     * Getter for destination.
     * @return Destination.
     */
    public ArrayList<String> getDestination() {
        return destination;
    }

    /**
     * Getter for user id.
     * @return User id.
     */
    public String getUserID() {
        return userID;
    }
}
