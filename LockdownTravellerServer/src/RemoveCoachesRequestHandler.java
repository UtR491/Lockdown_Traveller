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
        String query1="update basic_train_info set ?=?-? where Train_ID=?;";
        RemoveCoachesResponse removeCoachesResponse=removeCoaches(query1,numOfCoaches,Train_ID,coach);
        Server.SendResponse(oos,removeCoachesResponse);
    }


    public RemoveCoachesResponse removeCoaches(String query1, int numOfCoaches,String Train_ID,String coach) {
        String response;
        PreparedStatement preparedStatement;
        int c=0;
        try {
            preparedStatement = connection.prepareStatement(query1);
            preparedStatement.setString(1,coach);
            preparedStatement.setString(2,coach);
            preparedStatement.setInt(3,numOfCoaches);
            preparedStatement.setString(4,Train_ID);
            c=preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(c!=0){response=numOfCoaches+" number of coaches removed";}
        else {response="Could not remove coaches,Please try again";}
        return new RemoveCoachesResponse(response);
    }
}
