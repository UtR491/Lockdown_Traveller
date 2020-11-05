import java.io.Serializable;

/**
 * Request for the notification feature.
 */
public class NotificationRequest extends Request implements Serializable {

    final private String userID;

    /**
     * Used to construct the notification request object.
     * @param userID Identifier for the user who requested for notification.
     */
    NotificationRequest(String userID){
        this.userID= userID;
    }

    /**
     * Getter for user id.
     * @return User id.
     */
    public String getUserID() {
        return userID;
    }
}
