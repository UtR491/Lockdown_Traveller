import java.io.Serializable;

public class CancelTrainsRequest extends Request implements Serializable {
private String Train_ID,sDate;
CancelTrainsRequest(String Train_ID,String sDate)
{
    this.sDate=sDate;
    this.Train_ID=Train_ID;
}

    public String getTrain_ID() {
        return Train_ID;
    }

    public String getsDate() {
        return sDate;
    }
}
