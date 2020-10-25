import java.io.Serializable;
import java.util.ArrayList;

public class AddTrainsRequest extends  Request implements Serializable {
    String Train_ID,Train_Name,Days_Running,FirstAC_Coaches,SecondAC_Coaches,ThirdAC_Coaches,Sleeper_Coaches,FirstAC_Seats,SecondAC_Seats,ThirdAC_Seats,Sleeper_Seats,FirstAC_Fare,SecondAC_Fare,ThirdAC_Fare,Sleeper_Fare;
    String Added_Till;
    ArrayList<String>Station,City_Code,Arrival,Departure,Day_No,Distance_Covered;
    AddTrainsRequest(String Train_ID,String Train_Name,String Days_Running,String FirstAC_Coaches,
                     String SecondAC_Coaches,String ThirdAC_Coaches,String Sleeper_Coaches,String FirstAC_Seats,
                     String SecondAC_Seats,String ThirdAC_Seats,String Sleeper_Seats,String FirstAC_Fare,
                     String SecondAC_Fare,String ThirdAC_Fare,String Sleeper_Fare,String Added_Till,
                     ArrayList<String>Station,ArrayList<String>City_Code,ArrayList<String> Arrival,
                     ArrayList<String> Departure,ArrayList<String> Day_No,ArrayList<String> Distance_Covered){
        this.Added_Till=Added_Till;
        this.Arrival=Arrival;
        this.City_Code=City_Code;
        this.Day_No=Day_No;
        this.Days_Running=Days_Running;
        this.Departure=Departure;
        this.Distance_Covered=Distance_Covered;
        this.FirstAC_Coaches=FirstAC_Coaches;
        this.FirstAC_Fare=FirstAC_Fare;
        this.FirstAC_Seats=FirstAC_Seats;
        this.SecondAC_Coaches=SecondAC_Coaches;
        this.SecondAC_Fare=SecondAC_Fare;
        this.SecondAC_Seats=SecondAC_Seats;
        this.Sleeper_Coaches=Sleeper_Coaches;
        this.Sleeper_Fare=Sleeper_Fare;
        this.Sleeper_Seats=Sleeper_Seats;
        this.Station=Station;
        this.ThirdAC_Coaches=ThirdAC_Coaches;
        this.ThirdAC_Fare=ThirdAC_Fare;
        this.ThirdAC_Seats=ThirdAC_Seats;
        this.Train_ID=Train_ID;
        this.Train_Name=Train_Name;
    }

    public String getTrain_ID() {
        return Train_ID;
    }

    public ArrayList<String> getArrival() {
        return Arrival;
    }

    public ArrayList<String> getStation() {
        return Station;
    }

    public ArrayList<String> getCity_Code() {
        return City_Code;
    }

    public ArrayList<String> getDay_No() {
        return Day_No;
    }

    public ArrayList<String> getDeparture() {
        return Departure;
    }

    public String getAdded_Till() {
        return Added_Till;
    }

    public ArrayList<String> getDistance_Covered() {
        return Distance_Covered;
    }

    public String getDays_Running() {
        return Days_Running;
    }

    public String getFirstAC_Coaches() {
        return FirstAC_Coaches;
    }

    public String getFirstAC_Fare() {
        return FirstAC_Fare;
    }

    public String getFirstAC_Seats() {
        return FirstAC_Seats;
    }

    public String getSecondAC_Coaches() {
        return SecondAC_Coaches;
    }

    public String getSecondAC_Fare() {
        return SecondAC_Fare;
    }

    public String getSecondAC_Seats() {
        return SecondAC_Seats;
    }

    public String getSleeper_Coaches() {
        return Sleeper_Coaches;
    }

    public String getSleeper_Fare() {
        return Sleeper_Fare;
    }

    public String getSleeper_Seats() {
        return Sleeper_Seats;
    }

    public String getThirdAC_Coaches() {
        return ThirdAC_Coaches;
    }

    public String getThirdAC_Fare() {
        return ThirdAC_Fare;
    }

    public String getThirdAC_Seats() {
        return ThirdAC_Seats;
    }

    public String getTrain_Name() {
        return Train_Name;
    }
}

