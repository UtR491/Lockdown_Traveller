import java.io.Serializable;

public class BookingHistoryRequest extends  Request implements Serializable {
   private String userid;
    BookingHistoryRequest(String userid){
        this.userid=userid;
    }
    public String getUserid(){
        return userid;
    }
}
