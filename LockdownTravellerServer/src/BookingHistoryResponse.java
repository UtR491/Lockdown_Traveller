import java.io.Serializable;
import java.util.ArrayList;

public class BookingHistoryResponse extends Response implements Serializable {
    ArrayList<String> bookingID = null;
    ArrayList<String> name = null;
    ArrayList<Integer> age = null;
    ArrayList<String> gender=null;
    String pnr=null;
    String userid=null;

    public BookingHistoryResponse(ArrayList<String> bookingID, ArrayList<String> name, ArrayList<Integer> age,
                                  ArrayList<String> gender, String pnr, String userid) {
        this.bookingID = bookingID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.pnr = pnr;
        this.userid = userid;
    }

    public ArrayList<String> getBookingID() {
        return bookingID;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public ArrayList<Integer> getAge() {
        return age;
    }

    public ArrayList<String> getGender() {
        return gender;
    }

    public String getPnr() {
        return pnr;
    }

    public String getUserid() {
        return userid;
    }
}
