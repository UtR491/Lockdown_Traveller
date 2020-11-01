import java.io.Serializable;

public class ChatMessageRequest extends Request implements Serializable {
    private String message;
    private String ID;
    private String to;

    public ChatMessageRequest(String message, String userID, String to) {
        this.message = message;
        this.ID = ID;
        this.to = to;
    }

    public String getTo(){
        return this.to;
    }
    public String getMessage(){
        return this.message;
    }

    public String getID() {
        return ID;
    }
}
