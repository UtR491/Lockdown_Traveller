import java.io.Serializable;

public class RemoveTrainsRequest extends Request implements Serializable {
    final private String train_ID;
    RemoveTrainsRequest(String train_ID) {
        this.train_ID=train_ID;
    }

    public String getTrain_ID() {
        return train_ID;
    }
}
