import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCoachesRequestHandler extends Handler {
    AddCoachesRequest addCoachesRequest;
   Connection connection;
   ObjectOutputStream oos;
    public AddCoachesRequestHandler(Connection connection, AddCoachesRequest addCoachesRequest,ObjectOutputStream oos) {
        this.connection=connection;
        this.addCoachesRequest=addCoachesRequest;
        this.oos=oos;
    }

    @Override
    void sendQuery() {
        String Train_ID=addCoachesRequest.getTrain_ID();
        String coach=addCoachesRequest.getCoachType()+"_Coaches";
        int numOfCoaches=addCoachesRequest.getNumOfCoaches();

        String query1="update basic_train_info set ? = ?+? where Train_ID=?;";
        AddCoachesResponse addCoachesResponse=addCoaches(query1,numOfCoaches,coach,Train_ID);
        Server.SendResponse(oos,addCoachesResponse);
    }
    public AddCoachesResponse addCoaches(String query1, int numOfCoaches, String coach, String train_ID)
    {
        String response;
        PreparedStatement preparedStatement;
        int c=0;
        try {
            preparedStatement=connection.prepareStatement(query1);
            preparedStatement.setString(1,coach);
            preparedStatement.setString(2,coach);
            preparedStatement.setInt(3,numOfCoaches);
            preparedStatement.setString(4,train_ID);
            c=preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(c!=0){response=numOfCoaches+" Coaches added succesfully";}
        else {response="Could not add coaches,Please try again";}
        return new AddCoachesResponse(response);
    }
}
