import java.io.Serializable;
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
}
