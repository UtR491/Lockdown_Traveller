import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NotificationRequestHandler extends Handler {
    Connection connection;
    NotificationRequest notificationRequest;
    ObjectOutputStream oos;

    public NotificationRequestHandler(Connection connection,NotificationRequest notificationRequest, ObjectOutputStream oos) {
        this.connection = connection;
        this.notificationRequest=notificationRequest;
        this.oos = oos;
    }

    @Override
    void sendQuery() throws IOException, SQLException {
        String query1="select Message from Notification where User_ID=? and Pending_Status=1;";
        String query2="update Notification set Pending_Status=0 where User_ID=?;";
        NotificationResponse notificationResponse=notification(query1,query2,notificationRequest);
        Server.SendResponse(oos,notificationResponse);
    }
    public NotificationResponse notification(String query1,String query2,NotificationRequest notificationRequest)
    {
        PreparedStatement preparedStatement= null;
        ResultSet resultSet=null;
        ArrayList<String>message = null;

        try {
            preparedStatement = connection.prepareStatement(query1);
            preparedStatement.setString(1,notificationRequest.getUserID());
             resultSet=preparedStatement.executeQuery();
             preparedStatement=connection.prepareStatement(query2);
             preparedStatement.setString(1,notificationRequest.getUserID());
             preparedStatement.executeUpdate();
             while (resultSet.next())
             {
                 assert message != null;
                 message.add(resultSet.getString(1));
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new NotificationResponse(message,notificationRequest.getUserID());
    }
}

