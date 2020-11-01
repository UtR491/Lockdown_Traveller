import java.io.Serializable;
import java.util.ArrayList;

public class DisplayTrainsRunningTodayResponse extends Response implements Serializable {
    ArrayList<String>trainID,trainName;
    ArrayList<ArrayList<String>>station,departure,arrival;
    ArrayList<ArrayList<Integer>>stationNo;
    ArrayList<Integer>setPlatform;

    public DisplayTrainsRunningTodayResponse(ArrayList<String> trainID, ArrayList<String> trainName, ArrayList<ArrayList<String>> station, ArrayList<ArrayList<Integer>> stationNo, ArrayList<ArrayList<String>> departure, ArrayList<ArrayList<String>> arrival, ArrayList<Integer>setPlatform) {
        this.trainID = trainID;
        this.trainName = trainName;
        this.station = station;
        this.stationNo = stationNo;
        this.departure = departure;
        this.arrival = arrival;
        this.setPlatform=setPlatform;
    }
    public ArrayList<String> getTrainID() {
        return trainID;
    }

    public ArrayList<String> getTrainName() {
        return trainName;
    }

    public ArrayList<ArrayList<String>> getStation() {
        return station;
    }

    public ArrayList<ArrayList<Integer>> getStationNo() {
        return stationNo;
    }

    public ArrayList<ArrayList<String>> getDeparture() {
        return departure;
    }

    public ArrayList<ArrayList<String>> getArrival() {
        return arrival;
    }

    public ArrayList<Integer> getSetPlatform() {
        return setPlatform;
    }
}

class TrainsToday {
    private final String train;
    private final ArrayList<String> stations, arrival, departure;
    private final ArrayList<Integer> stationNo;

    public TrainsToday(String trainID, String trainName, ArrayList<String> stations, ArrayList<String> arrival,
                       ArrayList<String> departure, ArrayList<Integer> stationNo) {
        this.stations = stations;
        this.arrival = arrival;
        this.departure = departure;
        this.stationNo = stationNo;
        this.train = trainID + " " + trainName;
    }

    public ArrayList<String> getStations() {
        return stations;
    }

    public ArrayList<String> getArrival() {
        return arrival;
    }

    public ArrayList<String> getDeparture() {
        return departure;
    }

    public ArrayList<Integer> getStationNo() {
        return stationNo;
    }

    public String getTrain() {
        return train;
    }
}