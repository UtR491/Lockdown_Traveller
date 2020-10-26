import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewReroutedTrainsRequestHandler extends Handler {
    private Connection connection;
    private ObjectOutputStream oos;
    private ViewReroutedTrainsRequest viewReroutedTrainsRequest;
    ViewReroutedTrainsRequestHandler(Connection connection,ViewReroutedTrainsRequest viewReroutedTrainsRequest,ObjectOutputStream oos)
    {
        this.connection=connection;
        this.viewReroutedTrainsRequest=viewReroutedTrainsRequest;
        this.oos=oos;
    }

    @Override
    void sendQuery() {

        String query1="select Train_Name,Train_ID,Rerouted_Till from basic_train_info where Rerouted_Till in (select Rerouted_Till from basic_train_info where datediff(Rerouted_Till,?)>0);";
        String query2="select Station,Arrival,Departure from route_info where Train_ID=? and inCurrentRoute=1;";
    }
    public ViewReroutedTrainsResponse viewReroutedTrains(String query1,String query2)
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String sDate=simpleDateFormat.format(date);
        ResultSet resultSet=null;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query1);
            preparedStatement.setString(1,sDate);
            resultSet=preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
