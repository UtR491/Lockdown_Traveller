import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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
        String q="select Cancelled_Till,Train_ID from basic_train_info where Cancelled_Till is not null;";
        String query="select Train_Name,Train_ID,Cancelled_Till from basic_train_info where Train_ID=?;";
        ViewCancelledTrainsResponse viewCancelledTrainsResponse=viewCancelledTrains(q, query,viewCancelledTrainsRequest);
        Server.SendResponse(oos,viewCancelledTrainsResponse);
    }
    public ViewCancelledTrainsResponse viewCancelledTrains(String q,String query,ViewCancelledTrainsRequest viewCancelledTrainsRequest) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date reqdDate = null, cancelledTill;
        try {
            reqdDate = sdf.parse(viewCancelledTrainsRequest.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ResultSet resultSet , cancelledTrains;
        ArrayList<String> Train_Name = new ArrayList<>();
        ArrayList<String> Train_ID = new ArrayList<>();
        ArrayList<String> Cancelled_Till = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(q);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                cancelledTill = sdf.parse(resultSet.getString(1));
                assert reqdDate != null;
                int compare = cancelledTill.compareTo(reqdDate);
                if (compare > 0) {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, resultSet.getString(2));
                    cancelledTrains = preparedStatement.executeQuery();
                    while (cancelledTrains.next()) {
                        assert false;
                        Train_Name.add(cancelledTrains.getString(1));
                        assert false;
                        Train_ID.add(cancelledTrains.getString(2));
                        Cancelled_Till.add(cancelledTrains.getString(3));
                    }
                }
            }

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        return new ViewCancelledTrainsResponse(Train_ID, Train_Name, Cancelled_Till);
    }
}
