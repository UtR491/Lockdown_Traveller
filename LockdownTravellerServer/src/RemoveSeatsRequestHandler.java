import java.io.ObjectOutputStream;

public class RemoveSeatsRequestHandler extends Handler {
    RemoveSeatsRequest removeSeatsRequest;
    ObjectOutputStream oos;
    RemoveSeatsRequestHandler(RemoveSeatsRequest removeSeatsRequest,ObjectOutputStream oos)
    {
        this.oos=oos;
        this.removeSeatsRequest=removeSeatsRequest;
    }

    @Override
    void sendQuery() {
        String Train_ID=removeSeatsRequest.getTrain_ID();
        String coach=removeSeatsRequest.getCoach()+"_Seats";
        int numOfSeats=removeSeatsRequest.getNumOfSeats();
        String query1="select "+coach+" from basic_train_info where Train_ID=\""+Train_ID+"\";";
        String query2="update basic_train_info set "+coach+" = "+"xx"+" where Train_ID=\""+Train_ID+"\";";
        DatabaseConnector db=new DatabaseConnector();
        RemoveSeatsResponse removeSeatsResponse=db.removeSeats(query1,query2,numOfSeats);
        Server.SendResponse(oos,removeSeatsResponse);
    }
}
