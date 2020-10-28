import java.io.Serializable;
import java.util.ArrayList;

public class NotificationResponse extends Response implements Serializable {
    private ArrayList<String> message;
    private String userID;

    public NotificationResponse(ArrayList<String> message, String userID) {
        this.message = message;
        this.userID = userID;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public String getUserID() {
        return userID;
    }
}
