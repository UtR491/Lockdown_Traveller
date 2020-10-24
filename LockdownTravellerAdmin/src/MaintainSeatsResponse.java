import java.io.Serializable;
import java.util.ArrayList;

public class MaintainSeatsResponse extends Response implements Serializable {
    private final ArrayList<Train2> trains;
    public MaintainSeatsResponse(ArrayList<Train2> trains) {
        this.trains = trains;
    }

    public ArrayList<Train2> getTrains() {
        return trains;
    }
}

class Train2 implements Serializable {
    private final String trainId, trainName;
    private final ArrayList<Coach> coachInfo;
    public Train2(String trainId, String trainName, ArrayList<Coach> coachInfo) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.coachInfo = coachInfo;
    }

    @Override
    public String toString() {
        return trainId + " " + trainName;
    }
    public ArrayList<Coach> getCoachInfo() {
        return coachInfo;
    }
}

class Coach implements Serializable {
    final private String coach;
    final private int coaches, seats;
    public Coach(String coach, int coaches, int seats) {
        this.coach = coach;
        this.coaches = coaches;
        this.seats = seats;
    }

    public int getCoaches() {
        return coaches;
    }
    public String getCoach() {
        return coach;
    }
    public int getSeats() {
        return seats;
    }
}
