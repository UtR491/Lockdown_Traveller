import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CancelTrainsRequestHandler  extends Handler{
    Connection connection;
    CancelTrainsRequest cancelTrainsRequest;
    ObjectOutputStream oos;

    public CancelTrainsRequestHandler(Connection connection, CancelTrainsRequest cancelTrainsRequest,ObjectOutputStream oos) {
        this.cancelTrainsRequest=cancelTrainsRequest;
        this.connection=connection;
        this.oos=oos;
    }

    @Override
    void sendQuery(){
        String Train_ID=cancelTrainsRequest.getTrain_ID();
        String date=cancelTrainsRequest.getsDate();
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtf2=  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date= LocalDate.parse(date,dtf).format(dtf2);
        String query1="update Basic_Train_Info set Cancelled_Till = ? where Train_ID = ?;";
        String query2="select User_ID from User;";
        String query3="insert into notification values(?,?,1);";
        CancelTrainsResponse cancelTrainsResponse=cancelTrains(query1,query2,query3,Train_ID,date);
        Server.SendResponse(oos,cancelTrainsResponse);
    }
    public CancelTrainsResponse cancelTrains(String query1, String query2, String query3, String train_ID, String date)
    {
        String reponse=null;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query1);
            preparedStatement.setString(1,date);
            preparedStatement.setString(2, train_ID);
            int c=preparedStatement.executeUpdate();
            if(c!=0){
                preparedStatement=connection.prepareStatement(query2);
                ResultSet resultSet=preparedStatement.executeQuery();
                while (resultSet.next())
                {
                    preparedStatement=connection.prepareStatement(query3);
                    preparedStatement.setString(1,resultSet.getString("User_ID"));
                    preparedStatement.setString(2,"As of 11:59 PM today the train number"+train_ID+"has been cancelled untill"+date);
                    c=preparedStatement.executeUpdate();
                }
            }
            if(c!=0){reponse="success";}
            else {reponse="failure";}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new CancelTrainsResponse(reponse);
    }
}
