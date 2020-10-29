
import java.io.Serializable;
import java.util.ArrayList;

public class DisplayTrainsResponse extends Response implements Serializable
{
    final private ArrayList<String> Train_ID,Train_Name,Departure,Arrival,First_AC,Second_AC,Third_AC,Sleeper;
    final private String sDate,Source,Destination;
<<<<<<< HEAD
    final private int i;

    public DisplayTrainsResponse(ArrayList<String> Train_ID, ArrayList<String> Train_Name, String Source,
                                 ArrayList<String> Departure, String Destination, ArrayList<String> Arrival,
                                 ArrayList<String> First_AC, ArrayList<String> Second_AC, ArrayList<String> Third_AC,
                                 ArrayList<String> Sleeper, String sDate, int i) {
        this.Train_ID=Train_ID;
        this.Train_Name=Train_Name;
        this.Source=Source;
        this.Departure=Departure;
        this.Destination=Destination;
        this.Arrival=Arrival;
        this.First_AC=First_AC;
        this.Second_AC=Second_AC;
        this.Third_AC=Third_AC;
        this.Sleeper=Sleeper;
        this.sDate=sDate;
        this.i=i;
    }

    public ArrayList<String> getTrain_ID() {
        return Train_ID;
    }

    public ArrayList<String> getTrain_Name() {
        return Train_Name;
=======
    final private ArrayList<Integer>AC1Fare, AC2Fare,AC3Fare,SLFare;

    public DisplayTrainsResponse(ArrayList<String> train_ID, ArrayList<String> train_Name, ArrayList<String> departure, ArrayList<String> arrival, ArrayList<String> first_AC, ArrayList<String> second_AC, ArrayList<String> third_AC, ArrayList<String> sleeper, String sDate, String source, String destination,ArrayList<Integer>AC1Fare,ArrayList<Integer> AC2Fare,ArrayList<Integer>AC3Fare,ArrayList<Integer> SLFare) {
        Train_ID = train_ID;
        Train_Name = train_Name;
        Departure = departure;
        Arrival = arrival;
        First_AC = first_AC;
        Second_AC = second_AC;
        Third_AC = third_AC;
        Sleeper = sleeper;
        this.sDate = sDate;
        Source = source;
        Destination = destination;
        this.AC1Fare=AC1Fare;
        this.AC2Fare=AC2Fare;
        this.AC3Fare=AC3Fare;
        this.SLFare=SLFare;
    }

    public ArrayList<Integer> getAC1Fare() {
        return AC1Fare;
    }

    public ArrayList<Integer> getAC2Fare() {
        return AC2Fare;
    }

    public ArrayList<Integer> getAC3Fare() {
        return AC3Fare;
>>>>>>> f52dd2baa11b14f5d1c24046f6a11a9828585f23
    }

    public ArrayList<Integer> getSLFare() {
        return SLFare;
    }

<<<<<<< HEAD
    public ArrayList<String> getDeparture() {
        return Departure;
=======
    public ArrayList<String> getTrain_ID() {
        return Train_ID;
>>>>>>> f52dd2baa11b14f5d1c24046f6a11a9828585f23
    }

    public ArrayList<String> getTrain_Name() {
        return Train_Name;
    }

<<<<<<< HEAD
    public ArrayList<String> getArrival() {
        return Arrival;
=======
    public ArrayList<String> getDeparture() {
        return Departure;
>>>>>>> f52dd2baa11b14f5d1c24046f6a11a9828585f23
    }

    public ArrayList<String> getArrival() {
        return Arrival;
    }

    public ArrayList<String> getFirst_AC() {
        return First_AC;
    }

    public ArrayList<String> getSecond_AC() {
        return Second_AC;
    }

    public ArrayList<String> getThird_AC() {
        return Third_AC;
    }

    public ArrayList<String> getSleeper() {
        return Sleeper;
    }

    public String getsDate() {
        return sDate;
    }

    public String getSource() {
        return Source;
    }

    public String getDestination() {
        return Destination;
    }
}

