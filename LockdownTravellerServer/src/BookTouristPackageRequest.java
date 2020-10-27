import java.io.Serializable;
import java.util.ArrayList;

public class BookTouristPackageRequest extends Request implements Serializable {
    private ArrayList<String> name,dob,mealPreference;
    private ArrayList<Integer> age;
    private ArrayList<Character>gender;
    private String boardingPoint,userID,packageID;
    private int availableSeats;

    public BookTouristPackageRequest(ArrayList<String> name, ArrayList<String> dob, ArrayList<String> mealPreference, ArrayList<Integer> age, ArrayList<Character> gender, String boardingPoint, String userID, String packageID, int availableSeats) {
        this.name = name;
        this.dob = dob;
        this.mealPreference = mealPreference;
        this.age = age;
        this.gender = gender;
        this.boardingPoint = boardingPoint;
        this.userID = userID;
        this.packageID = packageID;
        this.availableSeats = availableSeats;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public ArrayList<String> getDob() {
        return dob;
    }

    public ArrayList<String> getMealPreference() {
        return mealPreference;
    }

    public ArrayList<Integer> getAge() {
        return age;
    }

    public ArrayList<Character> getGender() {
        return gender;
    }

    public String getBoardingPoint() {
        return boardingPoint;
    }

    public String getUserID() {
        return userID;
    }

    public String getPackageID() {
        return packageID;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
}
