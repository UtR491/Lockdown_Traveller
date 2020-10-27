import java.io.ObjectOutputStream;
import java.io.Serializable;

public class NotificationResponse extends Response implements Serializable {
    Message message;
    ObjectOutputStream outputStream;
//    server sendreponse method called

}
    class Message{

        private String PNR;
        long bookindId;
        RerouteResponse rerouteResponse;
        CancelTrainsResponse cancelTrainsResponse;

        public void Message (String PNR, long bookingId, RerouteResponse rerouteResponse, CancelTrainsResponse cancelTrainsResponse){

            this.PNR=PNR;
            this.bookindId=bookingId;
            this.rerouteResponse=rerouteResponse;
            this.cancelTrainsResponse=cancelTrainsResponse;

        }


    }


