
import java.io.Serializable;

public class DisplayTrainsResponse extends Response implements Serializable
{
    final private String [] Train_ID,Train_Name,Departure,Arrival,First_AC,Second_AC,Third_AC,Sleeper;
    final private String sDate,Source,Destination;
    final private int i;

    DisplayTrainsResponse(String [] Train_ID,String [] Train_Name,String  Source,String [] Departure,String  Destination,String [] Arrival,String [] First_AC,String [] Second_AC,String [] Third_AC,String [] Sleeper,String sDate,int i){
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

    public String[] getTrain_ID() {
        return Train_ID;
    }

    public String[] getTrain_Name() {
        return Train_Name;
    }

    public String getSource() {
        return Source;
    }

    public String[] getDeparture() {
        return Departure;
    }

    public String getDestination() {
        return Destination;
    }

    public String[] getArrival() {
        return Arrival;
    }

    public String getsDate() {
        return sDate;
    }

    public String[] getFirst_AC() {
        return First_AC;
    }

    public String[] getSecond_AC() {
        return Second_AC;
    }

    public String[] getThird_AC() {
        return Third_AC;
    }

    public String[] getSleeper() {
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