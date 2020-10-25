import java.io.Serializable;

public class RemoveCoachesRequest extends Request implements Serializable {
    String coachType;
    int numOfCoaches;
    String Train_ID;
    RemoveCoachesRequest(String Train_ID, String coachType, int numOfCoaches)
    {
        this.Train_ID=Train_ID;
        this.coachType=coachType;
        this.numOfCoaches=numOfCoaches;
    }

    public String getTrain_ID() {
        return Train_ID;
    }

    public int getNumOfCoaches() {
        return numOfCoaches;
    }

    public String getCoachType() {
        return coachType;
    }
}
