import java.io.Serializable;
import java.util.ArrayList;

public class NotificationResponse extends Response implements Serializable {
    private ArrayList<String> message;
    private String userID;
    private ArrayList<Integer>  pendingStatus;

    public NotificationResponse(ArrayList<String> message, String userID, ArrayList<Integer> pendingStatus) {
        this.message = message;
        this.userID = userID;
        this.pendingStatus = pendingStatus;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public ArrayList<Integer> getPendingStatus() {
        return pendingStatus;
    }

    public String getUserID() {
        return userID;
    }
}
