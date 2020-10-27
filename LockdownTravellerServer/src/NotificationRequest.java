import java.io.Serializable;

public class NotificationRequest extends Request implements Serializable {
    private int userID;
    NotificationRequest(int userID){
        this.userID= userID;
    }

    public int getUserID() {
        return userID;
    }
}
