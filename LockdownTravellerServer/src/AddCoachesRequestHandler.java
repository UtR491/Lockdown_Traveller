import java.io.ObjectOutputStream;
import java.sql.SQLException;

public class AddCoachesRequestHandler extends Handler {
    AddCoachesRequest addCoachesRequest;
    ObjectOutputStream oos;
    AddCoachesRequestHandler(AddCoachesRequest addCoachesRequest,ObjectOutputStream oos)
    {
        this.addCoachesRequest=addCoachesRequest;
        this.oos=oos;
    }

    @Override
    void sendQuery() throws SQLException {
        String Train_ID=addCoachesRequest.getTrain_ID();
        String coach=addCoachesRequest.getCoachType()+"_Coaches";
        int numOfCoaches=addCoachesRequest.getNumOfCoaches();
        String query1="select "+coach+" from basic_train_info where Train_ID=\""+Train_ID+"\";";
        String query2="update basic_train_info set "+coach+" = "+"xx"+" where Train_ID=\""+Train_ID+"\";";
        DatabaseConnector db=new DatabaseConnector();
        AddCoachesResponse addCoachesResponse=db.addCoaches(query1,query2,numOfCoaches);
        Server.SendResponse(oos,addCoachesResponse);
    }
}
