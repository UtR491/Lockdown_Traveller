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
