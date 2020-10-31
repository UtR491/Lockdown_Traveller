import java.io.Serializable;
import java.util.ArrayList;

public class ViewPlatformResponse extends Response implements Serializable {
    private String trainID;
  private ArrayList<String>station;
  private ArrayList<Integer>stationNo,platformNo;

    public ViewPlatformResponse(String trainID,ArrayList<String> station, ArrayList<Integer> platformNo,ArrayList<Integer>stationNo) {
        this.trainID=trainID;
        this.station = station;
        this.stationNo=stationNo;
        this.platformNo = platformNo;
    }

    public String getTrainID() {
        return trainID;
    }

    public ArrayList<Integer> getStationNo() {
        return stationNo;
    }

    public ArrayList<String> getStation() {
        return station;
    }

    public ArrayList<Integer> getPlatformNo() {
        return platformNo;
    }
}
