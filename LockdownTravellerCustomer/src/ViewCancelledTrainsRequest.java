import java.io.Serializable;

public class ViewCancelledTrainsRequest extends Request implements Serializable {
    String date;

    public ViewCancelledTrainsRequest(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
