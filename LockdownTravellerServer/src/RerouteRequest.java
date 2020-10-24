import java.io.Serializable;

public class RerouteRequest extends Request implements Serializable {
    String sDate,Train_ID,Station,Arrival,Departure,City_Code,Day_No,Distance_Covered,oldStation;
    Boolean inplace;

    public RerouteRequest(String sDate, String train_ID, String station, String arrival, String departure, String city_Code, String day_No, String distance_Covered, String oldStation, Boolean inplace) {
        this.sDate = sDate;
        Train_ID = train_ID;
        Station = station;
        Arrival = arrival;
        Departure = departure;
        City_Code = city_Code;
        Day_No = day_No;
        Distance_Covered = distance_Covered;
        this.oldStation = oldStation;
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

    public String getDay_No() {
        return Day_No;
    }

    public String getDistance_Covered() {
        return Distance_Covered;
    }

    public String getOldStation() {
        return oldStation;
    }

    public Boolean getInplace() {
        return inplace;
    }
}
