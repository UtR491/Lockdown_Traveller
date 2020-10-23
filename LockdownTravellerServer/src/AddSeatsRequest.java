import java.io.Serializable;

public class AddSeatsRequest extends Request implements Serializable {
    private String coach,Train_ID;
    private int numOfSeats;
    AddSeatsRequest(String Train_ID,String coach,int numOfSeats)
    {
        this.Train_ID=Train_ID;
        this.coach=coach;
        this.numOfSeats=numOfSeats;
    }

    public String getCoach() {
        return coach;
    }

    public String getTrain_ID() {
        return Train_ID;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }
}
