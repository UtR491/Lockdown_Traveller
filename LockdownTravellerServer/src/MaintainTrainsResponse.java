import java.io.Serializable;
import java.util.ArrayList;

public class MaintainTrainsResponse extends Response implements Serializable {

    private final ArrayList<Train> trains;

    public MaintainTrainsResponse(ArrayList<Train> trains) {
        this.trains = trains;
    }
    public ArrayList<Train> getTrains() {
        return trains;
    }
    public ArrayList<String> getString() {
        ArrayList<String> train = new ArrayList<>();
        for(Train t : trains) {
            train.add(t.toString());
        }
        return train;
    }
}

class Train implements Serializable{
    String trainId, trainName, days;
    ArrayList<String> route, arrival, departure;
    ArrayList<Integer> stationNumber, day;
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
    @Override
    public String toString() {
        return trainId + " " + trainName;
    }
    public String getTrainId() {
        return trainId;
    }
    public String getTrainName() {
        return trainName;
    }
    public String getDays() {
        return days;
    }
    public ArrayList<String> getRoute() {
        return route;
    }
    public ArrayList<String> getArrival() {
        return arrival;
    }
    public ArrayList<String> getDeparture() {
        return departure;
    }
    public ArrayList<Integer> getStationNumber() {
        return stationNumber;
    }
    public ArrayList<Integer> getDay() {
        return day;
    }
}
class Station {
    private String station, arrival , departure;
    private int day, stationNumber;

    public Station(String station, String arrival, String departure, int stationNumber, int day) {
        this.station = station;
        this.arrival = arrival;
        this.departure = departure;
        this.day = day;
        this.stationNumber = stationNumber;
    }

    public String getStation() {
        return station;
    }
    public String getArrival() {
        return arrival;
    }
    public String getDeparture() {
        return departure;
    }
    public int getDay() {
        return day;
    }
    public int getStationNumber() {
        return stationNumber;
    }
}
