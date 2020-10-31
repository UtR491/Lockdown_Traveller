import java.io.Serializable;

public class ViewPlatformRequest extends Request implements Serializable {
  private   String trainID;

    public ViewPlatformRequest(String trainID) {
        this.trainID = trainID;
    }

    public String getTrainID() {
        return trainID;
    }
}
