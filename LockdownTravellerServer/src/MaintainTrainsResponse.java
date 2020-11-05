import java.io.Serializable;
import java.util.ArrayList;

public class MaintainTrainsResponse extends Response implements Serializable {

    private final ArrayList<Train> trains;

    public MaintainTrainsResponse(ArrayList<Train> trains) {
        this.trains = trains;
    }

    /**
     * Getter for the list of all trains.
     * @return List of trains.
     */
    public ArrayList<Train> getTrains() {
        return trains;
    }

    /**
     * Name of trains (Train Name and id concatenated).
     * @return List of trian name.
     */
    public ArrayList<String> getString() {
        ArrayList<String> train = new ArrayList<>();
        for(Train t : trains) {
            train.add(t.toString());
        }
        return train;
    }
}

// Train class objects of which are the direct children of the root node, holds all information about the train and it's
// route.
class Train implements Serializable{

    String trainId, trainName, days;
    ArrayList<String> route, arrival, departure;
    ArrayList<Integer> stationNumber, day;

    /**
     * Constructor to initilaize the train object with all the information about a train.
     * @param trainId Identifier for the train.
     * @param trainName Name of the train.
     * @param days Days on which the train runs.
     * @param route Stations the train stops at in the journey.
     * @param arrival Arrival time at the corresponding station.
     * @param departure Departure time at the corresponding station.
     * @param stationNumber Position of the station in the Route. (This might not necessarily be the order in which they
     *                      are present in the array, since trains can be rerouted in the main version, but not in the
     *                      minimal version.
     * @param day Day of journey for the train.
     */
    public Train(String trainId, String trainName, String days, ArrayList<String> route, ArrayList<String> arrival,
                 ArrayList<String> departure, ArrayList<Integer> stationNumber, ArrayList<Integer> day) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.days = days;
        this.route = route;
        this.arrival = arrival;
        this.departure = departure;
        this.stationNumber = stationNumber;
        this.day = day;
    }

    /**
     * Train name to string.
     * @return Concatenated train name and ID to be displayed.
     */
    @Override
    public String toString() {
        return trainId + " " + trainName;
    }

    /**
     * Getter for the identifier for the train.
     * @return Train ID.
     */
    public String getTrainId() {
        return trainId;
    }

    /**
     * Getter for the name of the train.
     * @return Train name.
     */
    public String getTrainName() {
        return trainName;
    }

    /**
     * Getter for the days the train runs. It will be 7 digits and in binary format, 1 means the train runs on that day
     * 0 means it does not. "1000100" means the trains starts it's journey from the first station two days a week namely,
     * Monday and Friday.
     * @return Running days.
     */
    public String getDays() {
        return days;
    }

    /**
     * Getter for the station names.
     * @return List of station names.
     */
    public ArrayList<String> getRoute() {
        return route;
    }

    /**
     * Getter for arrival time at each station.
     * @return List of arrival time.
     */
    public ArrayList<String> getArrival() {
        return arrival;
    }

    /**
     * Getter for departure time from each station.
     * @return List of departure time.
     */
    public ArrayList<String> getDeparture() {
        return departure;
    }

    /**
     * Getter for station number.
     * @return Position of each station in the route.
     */
    public ArrayList<Integer> getStationNumber() {
        return stationNumber;
    }

    /**
     * Getter for day of journey.
     * @return Day of journey on which a train arrives at a particular station.
     */
    public ArrayList<Integer> getDay() {
        return day;
    }
}

/**
 * Leaf of the tree table view. Stores various information about a station leaf for a particular train.
 */
class Station {

    final private String station, arrival , departure;
    final private int day, stationNumber;

    /**
     * Constructor for Station class. Initializes the object with all the values that define a station.
     * @param station Name of the station.
     * @param arrival Arrival time of the train at the station.
     * @param departure Departure time of the train from the station.
     * @param stationNumber The position of the station in route of the train.
     * @param day The day wrt the first day of the journey.
     */
    public Station(String station, String arrival, String departure, int stationNumber, int day) {
        this.station = station;
        this.arrival = arrival;
        this.departure = departure;
        this.day = day;
        this.stationNumber = stationNumber;
    }

    /**
     * Getter for the station name.
     * @return Station name.
     */
    public String getStation() {
        return station;
    }

    /**
     * Getter for the arrival time.
     * @return Arrival time.
     */
    public String getArrival() {
        return arrival;
    }

    /**
     * Getter for the departure time.
     * @return Departure time.
     */
    public String getDeparture() {
        return departure;
    }

    /**
     * Getter for day of train's journey.
     * @return Day of journey.
     */
    public int getDay() {
        return day;
    }

    /**
     * Getter for position of the station on the trains route.
     * @return Station number.
     */
    public int getStationNumber() {
        return stationNumber;
    }
}
