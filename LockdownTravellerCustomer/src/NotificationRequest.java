import java.io.Serializable;

public class NotificationRequest extends Request implements Serializable {
    private String userID;
    NotificationRequest(String userID){
        this.userID= userID;
    }

    public String getUserID() {
        return userID;
    }
}
