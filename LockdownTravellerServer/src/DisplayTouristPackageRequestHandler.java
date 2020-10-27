import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplayTouristPackageRequestHandler extends Handler{
    ObjectOutputStream oos;
    Connection connection;
    DisplayTouristPackageRequest displayTouristPackageRequest;

    public DisplayTouristPackageRequestHandler(ObjectOutputStream oos, Connection connection, DisplayTouristPackageRequest displayTouristPackageRequest) {
        this.oos = oos;
        this.connection = connection;
        this.displayTouristPackageRequest = displayTouristPackageRequest;
    }

    @Override
    void sendQuery() throws SQLException {
        String query="select * from package_details;";
        DisplayTouristPackageResponse displayTouristPackageResponse=displayTouristPackage(query,displayTouristPackageRequest);
        Server.SendResponse(oos,displayTouristPackageResponse);
    }

   public DisplayTouristPackageResponse displayTouristPackage(String query,DisplayTouristPackageRequest displayTouristPackageRequest) throws SQLException {
       ResultSet resultSet = null;
       try {
           PreparedStatement preparedStatement=connection.prepareStatement(query);
            resultSet=preparedStatement.executeQuery();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       assert resultSet != null;
       return new DisplayTouristPackageResponse(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),resultSet.getString(10),resultSet.getString(11));
   }
}
