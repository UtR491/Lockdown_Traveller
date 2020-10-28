import java.io.Serializable;
import java.util.ArrayList;

public class DisplayTrainsResponse extends Response implements Serializable
{
    final private ArrayList<String> Train_ID,Train_Name,Departure,Arrival,First_AC,Second_AC,Third_AC,Sleeper;
    final private String sDate,Source,Destination;
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
    }

    public String getSource() {
        return Source;
    }

    public ArrayList<String> getDeparture() {
        return Departure;
    }

    public String getDestination() {
        return Destination;
    }

    public ArrayList<String> getArrival() {
        return Arrival;
    }

    public String getsDate() {
        return sDate;
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

    public int getI() {
        return i;
    }
}

class AvailableSeats implements Serializable
{
    private String Coach,Seats;
    AvailableSeats(String Coach,String Seats)
    {
        this.Coach=Coach;
        this.Seats=Seats;
    }

    public String getCoach() {
        return Coach;
    }

    public String getSeats() {
        return Seats;
    }
}