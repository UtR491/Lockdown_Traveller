import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class RemoveTrainsRequestHandler extends Handler {
    RemoveTrainsRequest removeTrainsRequest;
    ObjectOutputStream oos;
    RemoveTrainsRequestHandler(RemoveTrainsRequest removeTrainsRequest,ObjectOutputStream oos)
    {
        this.oos=oos;
        this.removeTrainsRequest=removeTrainsRequest;
    }

    @Override
    void sendQuery() {
        ArrayList<String>Train_ID=removeTrainsRequest.getTrain_ID();

        int status;
        String r = null;

        Iterator<String>iterator=Train_ID.iterator();
        while ((iterator.hasNext()))
        {
            String query1="delete from Basic_Train_Info where Train_ID=\""+iterator.next()+"\";";
            String query2="select User_ID from User;";
            String query3="insert into notifications values(\""+"xxxxx"+"\",\""+"As of 11:59 PM today the train number"+iterator.next()+"has been cancelled indefinitely"+"\",1);";
            DatabaseConnector db=new DatabaseConnector();
            RemoveTrainsResponse removeTrainsResponse=db.removeTrainsRequest(query1,query2,query3);
            status=removeTrainsResponse.getStatus();
            if(status!=0){r="Train removed successfully";}
            RemoveTrainsResponse removeTrainsResponse1=new RemoveTrainsResponse(r);
            Server.SendResponse(oos,removeTrainsResponse1);
        }
    }
}
