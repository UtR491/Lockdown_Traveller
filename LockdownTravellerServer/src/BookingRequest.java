import java.io.Serializable;
import java.time.LocalDate;

/**
 * Request class for booking a ticket in the train.
 */
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

    /**
     * Constructor for Booking Request. Initializes all the fields as per the input by user.
     * @param source Boarding Station.
     * @param destination De-boarding Station.
     * @param trainId Unique identifier for the train in which seat(s) have to be reserved.
     * @param coach Type of coach in which the seat(s) have to be reserved. Namely Sleeper, First AC, Second AC or
     *              Third AC.
     * @param date Date of boarding.
     * @param name Names of co-passengers.
     * @param age Ages of co-passengers.
     * @param gender Gender of co-passengers.
     * @param userId Unique identifier for the User who reserved the seats for everybody.
     * @param availableSeat Number of seats available in the coach, in the train for that journey. Calculated and
     *                      displayed in the DisplayTrainsRequest handler and on Display Trains screen respectively and
     *                      passed with the request to prevent re-calculation.
     * @param preference Seat preference of co-passengers. Namely Upper, Middle, Lower, Side-Upper, Side-Lower.
     * @param quota Quota of co-passengers. Currently only General and Viklang Quota.
     * @param meal Meal preference of every co-passenger.
     * @param numSeat Number of co-passengers.
     * @param totalCost Product of numSeat and fare per seat.
     */
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

    /**
     * Getter for train id.
     * @return Unique identifier for the train.
     */
    public String getTrainId() {
        return trainId;
    }

    /**
     * Getter for date of journey.
     * @return Date of journey.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Getter for boarding station.
     * @return Boarding station.
     */
    public String getSource() {
        return source;
    }

    /**
     * Getter for de-boarding station.
     * @return De-boarding station.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Getter for per-person preference.
     * @return Preference array.
     */
    public String[] getPreference() {
        return preference;
    }

    /**
     * Getter for coach in which the seat has to be booked.
     * @return Coach in which seats have to be reserved.
     */
    public String getCoach() {
        return coach;
    }

    /**
     * Names of co-passengers.
     * @return Name array.
     */
    public String[] getName() { return name; }

    /**
     * Ages of co-passengers.
     * @return Age array.
     */
    public int[] getAge() { return age; }

    /**
     * Gender of co-passengers.
     * @return Gender array.
     */
    public char[] getGender() { return gender; }

    /**
     * Identifier for the user who booked the seats.
     * @return User id.
     */
    public String getUserId() { return userId; }

    /**
     * Available seats.
     * @return Available seats.
     */
    public int getAvailableSeat() { return availableSeat; }

    /**
     * Number of seats to be reserved.
     * @return Number of seats.
     */
    public int getNumSeat() {
        return numSeat;
    }

    /**
     * Total cost of tickets.
     * @return Total costs.
     */
    public int getTotalCost() {
        return totalCost;
    }

    /**
     * Quota of each passenger.
     * @return Quota array.
     */
    public String[] getQuota() {
        return quota;
    }

    /**
     * Meal option for each passenger.
     * @return Meal array.
     */
    public int[] getMeal() {
        return meal;
    }
}
