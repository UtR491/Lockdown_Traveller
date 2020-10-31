import java.io.Serializable;

public class ChatMessageRequest extends Request implements Serializable {
    private String message;
    private String userID;
    private String to;

    public ChatMessageRequest(String message, String userID, String to) {
        this.message = message;
        this.userID = userID;
        this.to = to;
    }

    public String getTo(){
        return this.to;
    }
    public String getMessage(){
        return this.message;
    }



    public String toString() {
        return String.format("Message : %s\nFrom : %s\nTo : admin"
                , message, userID);
    }
}
