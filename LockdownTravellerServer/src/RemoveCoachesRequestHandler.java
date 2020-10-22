import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

public class RemoveCoachesRequestHandler extends Handler {
    RemoveCoachesRequest removeCoachesRequest;
    ObjectOutputStream oos;
    RemoveCoachesRequestHandler(RemoveCoachesRequest removeCoachesRequest,ObjectOutputStream oos)
    {
        this.oos=oos;
        this.removeCoachesRequest=removeCoachesRequest;
    }

    @Override
    void sendQuery() throws IOException, SQLException {
        String Train_ID=removeCoachesRequest.getTrain_ID();
        String coach=removeCoachesRequest.getCoachType()+"_Coaches";
        int numOfCoaches=removeCoachesRequest.getNumOfCoaches();
        String query1="select "+coach+" from basic_train_info where Train_ID=\""+Train_ID+"\";";
        String query2="update basic_train_info set "+coach+" = "+"xx"+" where Train_ID=\""+Train_ID+"\";";
        DatabaseConnector db=new DatabaseConnector();
        RemoveCoachesResponse removeCoachesResponse=db.removeCoaches(query1,query2,numOfCoaches);
        Server.SendResponse(oos,removeCoachesResponse);
    }
}
