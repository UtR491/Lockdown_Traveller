import java.io.Serializable;
import java.util.ArrayList;

public class SetPlatformRequest extends Request implements Serializable {
    ArrayList<String>trainID;
    ArrayList<ArrayList<Integer>>stationNo,platformNo;

    public SetPlatformRequest(ArrayList<String> trainID, ArrayList<ArrayList<Integer>> stationNo, ArrayList<ArrayList<Integer>> platformNo) {
        this.trainID = trainID;
        this.stationNo = stationNo;
        this.platformNo = platformNo;
    }

    public ArrayList<String> getTrainID() {
        return trainID;
    }

    public ArrayList<ArrayList<Integer>> getStationNo() {
        return stationNo;
    }

    public ArrayList<ArrayList<Integer>> getPlatformNo() {
        return platformNo;
    }
}
