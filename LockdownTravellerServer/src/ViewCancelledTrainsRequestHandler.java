import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class ViewCancelledTrainsRequestHandler extends Handler {
    private Connection connection;
    private ObjectOutputStream oos;
    private ViewCancelledTrainsRequest viewCancelledTrainsRequest;
    ViewCancelledTrainsRequestHandler(Connection connection,ViewCancelledTrainsRequest viewCancelledTrainsRequest,ObjectOutputStream oos)
    {
        this.connection=connection;
        this.oos=oos;
        this.viewCancelledTrainsRequest=viewCancelledTrainsRequest;
    }

    @Override
    void sendQuery() {

        String query="select Train_Name,Train_ID,Cancelled_Till from basic_train_info where Cancelled_Till in (select Cancelled_Till from basic_train_info where datediff(Cancelled_Till,?)>0);";
        ViewCancelledTrainsResponse viewCancelledTrainsResponse=viewCancelledTrains(query,viewCancelledTrainsRequest);
        Server.SendResponse(oos,viewCancelledTrainsResponse);
    }
    public ViewCancelledTrainsResponse viewCancelledTrains(String query,ViewCancelledTrainsRequest viewCancelledTrainsRequest)
    {
        String date=viewCancelledTrainsRequest.getDate();
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtf2=  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date= LocalDate.parse(date,dtf).format(dtf2);
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,date);
            resultSet=preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> Train_Name = null,Train_ID = null,Cancelled_Till= new ArrayList<>();
        while (true)
        {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Train_Name.add(resultSet.getString(1));
                Train_ID.add(resultSet.getString(2));
                Cancelled_Till.add(resultSet.getString(3));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new ViewCancelledTrainsResponse(Train_ID,Train_Name,Cancelled_Till);
    }
}
