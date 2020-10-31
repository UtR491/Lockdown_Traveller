import java.io.Serializable;

public class RerouteRequest extends Request implements Serializable {
    String sDate,Train_ID,Station,City_Code,oldStation,prevStation,nextStation,arrivalD,departureD,arrivalC,departureB;
    Boolean inplace;
    int distancePrev,distanceOld,distanceNext,dayNo;

    public RerouteRequest(String sDate, String Train_ID, String Station, String City_Code,int distancePrev,
                          int distanceOld, int distanceNext, String oldStation, String prevStation, String nextStation,
                          Boolean inplace,String arrivalD,String departureD,String arrivalC,String departureB,int dayNo) {
        this.sDate = sDate;
        this.Train_ID = Train_ID;
        this.Station = Station;
        this.City_Code = City_Code;
        this.distancePrev = distancePrev;
        this.distanceOld = distanceOld;
        this.distanceNext = distanceNext;
        this.oldStation = oldStation;
        this.prevStation = prevStation;
        this.nextStation = nextStation;
        this.inplace = inplace;
        this.arrivalD=arrivalD;
        this.departureD=departureD;
        this.arrivalC=arrivalC;
        this.dayNo=dayNo;
        this.departureB=departureB;
    }

    public String getDepartureB() {
        return departureB;
    }

    public int getDayNo() {
        return dayNo;
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
    public String getCity_Code() {
        return City_Code;
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

    public String getArrivalC() {
        return arrivalC;
    }

    public String getArrivalD() {
        return arrivalD;
    }

    public String getDepartureD() {
        return departureD;
    }
}
