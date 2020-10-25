import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveCoachesRequestHandler extends Handler {
    RemoveCoachesRequest removeCoachesRequest;
    Connection connection;
    ObjectOutputStream oos;
    public RemoveCoachesRequestHandler(Connection connection, RemoveCoachesRequest removeCoachesRequest, ObjectOutputStream oos) {
        this.connection=connection;
        this.removeCoachesRequest=removeCoachesRequest;
        this.oos=oos;
    }

    @Override
    void sendQuery() {
        String Train_ID=removeCoachesRequest.getTrain_ID();
        String coach=removeCoachesRequest.getCoachType()+"_Coaches";
        int numOfCoaches=removeCoachesRequest.getNumOfCoaches();
        String query1="update Basic_Train_Info set " + coach + " = " + coach + "-? where Train_ID=?;";
        RemoveCoachesResponse removeCoachesResponse=removeCoaches(query1,numOfCoaches,Train_ID,coach);
        Server.SendResponse(oos,removeCoachesResponse);
    }


    public RemoveCoachesResponse removeCoaches(String query1, int numOfCoaches,String Train_ID,String coach) {
        String response;
        PreparedStatement preparedStatement;
        int c=0;
        try {
            preparedStatement = connection.prepareStatement(query1);
            preparedStatement.setInt(1,numOfCoaches);
            preparedStatement.setString(2,Train_ID);
            c=preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(c!=0){response="success";}
        else {response="failure";}
        return new RemoveCoachesResponse(response);
    }
}
