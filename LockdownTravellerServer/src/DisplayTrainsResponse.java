import java.io.Serializable;
import java.util.ArrayList;

/**
 * Response object for display trains request.
 */
public class DisplayTrainsResponse extends Response implements Serializable
{
    final private ArrayList<String> Train_ID,Train_Name,Departure,Arrival,First_AC,Second_AC,Third_AC,Sleeper;
    final private String sDate,Source,Destination;
    final private ArrayList<Integer>AC1Fare, AC2Fare,AC3Fare,SLFare;

    /**
     * Constructor for the DisplayTrainsResponse. Initializes the response object with various data related to the trains
     * available for a specified journey.
     * @param train_ID Identifier for the train available.
     * @param train_Name Name of the train.
     * @param departure Time the trains leave the source station.
     * @param arrival Time the trains arrive at the destination.
     * @param first_AC Number of seats in First AC coach.
     * @param second_AC Number of seats in Second AC coach.
     * @param third_AC Number of seats in Third AC coach.
     * @param sleeper Number of seats in Sleeper coach.
     * @param sDate Date of journey.
     * @param source De-boarding station, not needed immediately, this information is passed and used in
     *               BookingRequest.
     * @param destination Boarding station.
     * @param AC1Fare Per seat rate for First AC coach.
     * @param AC2Fare Per seat rate for Second AC coach.
     * @param AC3Fare Per seat rate for Third AC coach.
     * @param SLFare Per seat rate for Sleeper coach.
     */
    public DisplayTrainsResponse(ArrayList<String> train_ID, ArrayList<String> train_Name, ArrayList<String> departure, ArrayList<String> arrival, ArrayList<String> first_AC, ArrayList<String> second_AC, ArrayList<String> third_AC, ArrayList<String> sleeper, String sDate, String source, String destination,ArrayList<Integer>AC1Fare,ArrayList<Integer> AC2Fare,ArrayList<Integer>AC3Fare,ArrayList<Integer> SLFare) {

        Train_ID = train_ID;
        Train_Name = train_Name;
        Departure = departure;
        Arrival = arrival;
        First_AC = first_AC;
        Second_AC = second_AC;
        Third_AC = third_AC;
        Sleeper = sleeper;
        this.sDate = sDate;
        Source = source;
        Destination = destination;
        this.AC1Fare=AC1Fare;
        this.AC2Fare=AC2Fare;
        this.AC3Fare=AC3Fare;
        this.SLFare=SLFare;
    }

    /**
     * Getter for rate of First AC fare in each train.
     * @return First AC fare array.
     */
    public ArrayList<Integer> getAC1Fare() {
        return AC1Fare;
    }

    /**
     * Getter for rate of Second AC fare in each train.
     * @return Second AC fare array.
     */
    public ArrayList<Integer> getAC2Fare() {
        return AC2Fare;
    }

    /**
     * Getter for rate of Third AC fare in each train.
     * @return Third AC fare array.
     */
    public ArrayList<Integer> getAC3Fare() {
        return AC3Fare;
    }

    /**
     * Getter for rate of Sleeper fare in each train.
     * @return Sleeper fare array.
     */
    public ArrayList<Integer> getSLFare() {
        return SLFare;
    }

    /**
     * Getter for identifier of each train available for the journey.
     * @return Train Identifier array..
     */
    public ArrayList<String> getTrain_ID() {
        return Train_ID;
    }

    /**
     * Getter for name of all the trains available for the journey.
     * @return Train name array.
     */
    public ArrayList<String> getTrain_Name() {
        return Train_Name;
    }

    /**
     * Getter for departure time for all trains.
     * @return Departure time array.
     */
    public ArrayList<String> getDeparture() {
        return Departure;
    }

    /**
     * Getter for arrival times for each train available for the journey.
     * @return Arrival time array.
     */
    public ArrayList<String> getArrival() {
        return Arrival;
    }

    /**
     * Getter for number of seats available in First AC coach of each train.
     * @return Number of seats available in the first ac coach for the journey.
     */
    public ArrayList<String> getFirst_AC() {
        return First_AC;
    }

    /**
     * Getter for number of seats available in Second AC coach of each train.
     * @return Number of seats available in the second ac coach for the journey.
     */
    public ArrayList<String> getSecond_AC() {
        return Second_AC;
    }

    /**
     * Getter for number of seats available in Third AC coach of each train.
     * @return Number of seats available in the third ac coach for the journey.
     */
    public ArrayList<String> getThird_AC() {
        return Third_AC;
    }

    /**
     * Getter for number of seats available in Sleeper coach of each train.
     * @return Number of seats available in the sleeper coach for the journey.
     */
    public ArrayList<String> getSleeper() {
        return Sleeper;
    }

    /**
     * Getter for the date of journey.
     * @return Date of journey.
     */
    public String getsDate() {
        return sDate;

    }

    /**
     * Getter for boarding station.
     * @return Boarding station.
     */
    public String getSource() {
        return Source;
    }

    /**
     * Getter for de-boarding station.
     * @return De-boarding station.
     */
    public String getDestination() {
        return Destination;
    }
}


/**
 * Available seats class. Used to initialize leaf nodes in TreeTableView.
 */
class AvailableSeats implements Serializable
{
    private String Coach,Seats;
    private int fare;

    /**
     * Constructs a node for TreeTableView.
     * @param Coach Type of coach.
     * @param Seats Number of seats available in the coach before booking.
     * @param fare Per seat cost of the specified journey.
     */
    AvailableSeats(String Coach,String Seats, int fare)
    {
        this.Coach=Coach;
        this.Seats=Seats;
        this.fare = fare;

    }

    /**
     * Getter for number of seats available.
     * @return Number of seats available in the coach.
     */
    public String getSeats() {
        return Seats;
    }

    /**
     * Getter for the coach.
     * @return The type of coach.
     */
    public String getCoach() {
        return Coach;
    }

    /**
     * Getter for coach fare.
     * @return Coach fare.
     */
    public int getFare() {
        return fare;
    }
}

