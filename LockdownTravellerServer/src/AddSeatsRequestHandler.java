import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddSeatsRequestHandler extends Handler {
    Connection connection;
    AddSeatsRequest addSeatsRequest;
    ObjectOutputStream oos;
    public AddSeatsRequestHandler(Connection connection, AddSeatsRequest addSeatsRequest,ObjectOutputStream oos) {
        this.connection=connection;
        this.addSeatsRequest=addSeatsRequest;
        this.oos=oos;
    }

    @Override
    void sendQuery() {
        String Train_ID=addSeatsRequest.getTrain_ID();
        String coach=addSeatsRequest.getCoach()+"_Seats";
        int numOfSeats=addSeatsRequest.getNumOfSeats();
        String query1="update basic_train_info set ?=?+? where Train_ID=?;";
        AddSeatsResponse addSeatsResponse=addSeats(query1,numOfSeats,Train_ID,coach);
        Server.SendResponse(oos,addSeatsResponse);
    }
    public AddSeatsResponse addSeats(String query1,int numOfSeats,String Train_ID,String coach)
    {
        String response;
        PreparedStatement preparedStatement;
        int c=0;
        try {
            preparedStatement = connection.prepareStatement(query1);
            preparedStatement.setString(1,coach);
            preparedStatement.setString(2,coach);
            preparedStatement.setInt(3,numOfSeats);
            preparedStatement.setString(4,Train_ID);
            c=preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(c!=0){response=numOfSeats +"number of seats added";}
        else {response="Could not add seats,Please try again";}
        return new AddSeatsResponse(response);

    }
}
