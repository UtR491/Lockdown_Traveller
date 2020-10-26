import java.io.Serializable;

public class RerouteRequest extends Request implements Serializable {
    String sDate,Train_ID,Station,Arrival,Departure,City_Code,oldStation,prevStation,nextStation;
    Boolean inplace;
    int Day_No,distancePrev,distanceOld,distanceNext;

    public RerouteRequest(String sDate, String Train_ID, String Station, String Arrival, String Departure,
                          String City_Code, int Day_No, int distancePrev, int distanceOld, int distanceNext,
                          String oldStation, String prevStation, String nextStation, Boolean inplace) {
        this.sDate = sDate;
        this.Train_ID = Train_ID;
        this.Station = Station;
        this.Arrival = Arrival;
        this.Departure = Departure;
        this.City_Code = City_Code;
        this.Day_No = Day_No;
        this.distancePrev = distancePrev;
        this.distanceOld = distanceOld;
        this.distanceNext = distanceNext;
        this.oldStation = oldStation;
        this.prevStation = prevStation;
        this.nextStation = nextStation;
        this.inplace = inplace;
    }

    public String getsDate() {
        return sDate;
    }

    public String getTrain_ID() {
        return Train_ID;
    }

    public String getStation() {
        return Station;
    }

    public String getArrival() {
        return Arrival;
    }

    public String getDeparture() {
        return Departure;
    }

    public String getCity_Code() {
        return City_Code;
    }

    public int getDay_No() {
        return Day_No;
    }

    public int getDistancePrev() {
        return distancePrev;
    }

    public int getDistanceOld() {
        return distanceOld;
    }

    public int getDistanceNext() {
        return distanceNext;
    }

    public String getOldStation() {
        return oldStation;
    }

    public String getPrevStation() {
        return prevStation;
    }

    public String getNextStation() {
        return nextStation;
    }

    public Boolean getInplace() {
        return inplace;
    }
}
