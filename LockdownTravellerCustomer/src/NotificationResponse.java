import java.io.Serializable;
import java.util.ArrayList;

/**
 * Response given by the server after the notification request has been processed.
 */
public class NotificationResponse extends Response implements Serializable {

    final private ArrayList<String> message;
    final private String userID;
    final private ArrayList<Integer>  pendingStatus;

    /**
     * Constructor to initialize the respones object.
     * @param message The notification itself.
     * @param userID ID of the user for whom the notification is intended.
     * @param pendingStatus 1 if the user will be sent this particular notification for the first time 0 if the user has
     *                     already once seen it.
     */
    public NotificationResponse(ArrayList<String> message, String userID, ArrayList<Integer> pendingStatus) {
        this.message = message;
        this.userID = userID;
        this.pendingStatus = pendingStatus;
    }

    /**
     * Getter for the notification message
     * @return Notification message.
     */
    public ArrayList<String> getMessage() {
        return message;
    }

    /**
     * Getter for the pending status of the corresponding message.
     * @return Pending status.
     */
    public ArrayList<Integer> getPendingStatus() {
        return pendingStatus;
    }

    /**
     * Getter for the user id of the user for who the message was intended to.
     * @return User id.
     */
    public String getUserID() {
        return userID;
    }
}
