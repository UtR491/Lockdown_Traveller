import java.io.ObjectOutputStream;

public class AddSeatsRequestHandler extends Handler {
    ObjectOutputStream oos;
    AddSeatsRequest addSeatsRequest;
    AddSeatsRequestHandler(AddSeatsRequest addSeatsRequest,ObjectOutputStream oos)
    {
        this.addSeatsRequest=addSeatsRequest;
        this.oos=oos;
    }

    @Override
    void sendQuery() {
        String Train_ID=addSeatsRequest.getTrain_ID();
        String coach=addSeatsRequest.getCoach()+"_Seats";
        int numOfSeats=addSeatsRequest.getNumOfSeats();
        String query1="select "+coach+" from basic_train_info where Train_ID=\""+Train_ID+"\";";
        String query2="update basic_train_info set "+coach+" = "+"xx"+" where Train_ID=\""+Train_ID+"\";";
        DatabaseConnector db=new DatabaseConnector();
        AddSeatsResponse addSeatsResponse=db.addSeats(query1,query2,numOfSeats);
        Server.SendResponse(oos,addSeatsResponse);
    }
}
