import java.io.Serializable;
import java.util.ArrayList;

public class RemoveTrainsRequest extends Request implements Serializable {
    private ArrayList<String>Train_ID=new ArrayList<String>();
    RemoveTrainsRequest(ArrayList<String>Train_ID)
    {
        this.Train_ID=Train_ID;
    }

    public ArrayList<String> getTrain_ID() {
        return Train_ID;
    }
}
