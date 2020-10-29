import java.io.Serializable;
import java.util.ArrayList;

public class ViewCancelledTrainsResponse extends Response implements Serializable {
    private ArrayList<String> Train_ID, Train_Name, Cancelled_Till;

    public ViewCancelledTrainsResponse(ArrayList<String> Train_ID, ArrayList<String> Train_Name, ArrayList<String> Cancelled_Till) {
        this.Train_ID = Train_ID;
        this.Train_Name = Train_Name;
        this.Cancelled_Till = Cancelled_Till;
    }

    public ArrayList<String> getTrain_ID() {
        return Train_ID;
    }

    public ArrayList<String> getTrain_Name() {
        return Train_Name;
    }

    public ArrayList<String> getCancelled_Till() {
        return Cancelled_Till;
    }
}
