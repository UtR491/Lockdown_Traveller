import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveTrainsRequestHandler extends Handler {
    RemoveTrainsRequest removeTrainsRequest;
    Connection connection;
    ObjectOutputStream oos;

    public RemoveTrainsRequestHandler(Connection connection, RemoveTrainsRequest removeTrainsRequest,ObjectOutputStream oos) {
        this.connection=connection;
        this.removeTrainsRequest=removeTrainsRequest;
        this.oos=oos;
    }

    @Override
    void sendQuery() {

        String train_ID=removeTrainsRequest.getTrain_ID();
        String query1 = "delete from Basic_Train_Info where Train_ID=?;";
        String query2 = "select User_ID from User;";
        String query3 = "insert into notifications values(?,?,1);";
        RemoveTrainsResponse removeTrainsResponse = removeTrainsRequest(query1, query2, query3, train_ID);
        Server.SendResponse(oos,removeTrainsResponse);
    }
    public RemoveTrainsResponse removeTrainsRequest(String query1,String query2,String query3,String train_ID)
    {
        int cancelStatus=0;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query1);
            preparedStatement.setString(1,train_ID);
            cancelStatus=preparedStatement.executeUpdate();
            if(cancelStatus!=0)
            {
                preparedStatement=connection.prepareStatement(query2);
                ResultSet resultSet=preparedStatement.executeQuery();
                while (resultSet.next())
                {
                    preparedStatement=connection.prepareStatement(query3);
                    preparedStatement.setString(1,resultSet.getString("User_ID"));
                    preparedStatement.setString(2,"As of 11:59 PM today the train number "+train_ID+" has been cancelled indefinitely");
                    cancelStatus=preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new RemoveTrainsResponse(cancelStatus != 0 ? "success" : "failure");
    }
}
