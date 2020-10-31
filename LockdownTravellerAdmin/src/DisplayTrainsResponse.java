import java.io.Serializable;
import java.util.ArrayList;

public class DisplayTrainsResponse extends Response implements Serializable
{
    final private ArrayList<String> Train_ID,Train_Name,Departure,Arrival,First_AC,Second_AC,Third_AC,Sleeper;
    final private String sDate,Source,Destination;
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
    }

    public ArrayList<Integer> getSLFare() {
        return SLFare;
    }

    public ArrayList<String> getTrain_ID() {
        return Train_ID;
    }

    public ArrayList<String> getTrain_Name() {
        return Train_Name;
    }

    public ArrayList<String> getDeparture() {
        return Departure;
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


class AvailableSeats implements Serializable
{
    private String Coach,Seats;
    private int fare;
    AvailableSeats(String Coach,String Seats, int fare)
    {
        this.Coach=Coach;
        this.Seats=Seats;
        this.fare = fare;

    }

    public String getSeats() {
        return Seats;
    }

    public String getCoach() {
        return Coach;
    }

    public int getFare() {
        return fare;
    }
}

