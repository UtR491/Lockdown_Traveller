import java.io.Serializable;
import java.util.ArrayList;

public class MaintainSeatsResponse extends Response implements Serializable {
    private final ArrayList<Train2> trains;
    public MaintainSeatsResponse(ArrayList<Train2> trains) {
        this.trains = trains;
    }

    /**
     * Getter for the list of trains.
     * @return Trains array.
     */
    public ArrayList<Train2> getTrains() {
        return trains;
    }
}

// Direct children of root node of the TreeTableView.
class Train2 implements Serializable {

    private final String trainId, trainName;
    private final ArrayList<Coach> coachInfo;

    /**
     * Constructor used to initialize a train object.
     * @param trainId Identifier for the trains.
     * @param trainName Name of the trains.
     * @param coachInfo Array of Coach type. Holds information about each type of coach in the train.
     */
    public Train2(String trainId, String trainName, ArrayList<Coach> coachInfo) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.coachInfo = coachInfo;
    }

    /**
     * Concatenates the train name and train ID to show as name.
     * @return Train name.
     */
    @Override
    public String toString() {
        return trainId + " " + trainName;
    }

    /**
     * Getter for Coach information of the train.
     * @return Coach information.
     */
    public ArrayList<Coach> getCoachInfo() {
        return coachInfo;
    }
}

// Nodes of the TreeTableView.
class Coach implements Serializable {

    final private String coach;
    final private int coaches, seats;

    /**
     * Constructor for the leaf node.
     * @param coach Name of the coach.
     * @param coaches Number of coaches in the coach.
     * @param seats Seats per coach in the specified coach.
     */
    public Coach(String coach, int coaches, int seats) {
        this.coach = coach;
        this.coaches = coaches;
        this.seats = seats;
    }

    /**
     * Getter for the number of coaches of type this.coach in a particular train.
     * @return Number of coaches.
     */
    public int getCoaches() {
        return coaches;
    }

    /**
     * Getter for the type of coach. Sleeper, First AC, Second AC or Third AC.
     * @return Type of coach.
     */
    public String getCoach() {
        return coach;
    }

    /**
     * Getter for the number of seats per coach.
     * @return Seats per coach.
     */
    public int getSeats() {
        return seats;
    }
}
