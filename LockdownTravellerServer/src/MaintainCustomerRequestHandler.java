import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaintainCustomerRequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private MaintainCustomerRequest request;
    public MaintainCustomerRequestHandler(ObjectOutputStream oos, Connection connection, MaintainCustomerRequest request) {
        System.out.println("Constructor of Maintain Customer Request Handler");
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }
    public void sendQuery() {
        String query = "select * from User;";
        MaintainCustomerResponse maintainCustomerResponse = maintainCustomerRequest(query);
        System.out.println("Sending the maintainCustomerResponse object over the socket");
        Server.SendResponse(oos, maintainCustomerResponse);
    }

    private MaintainCustomerResponse maintainCustomerRequest(String query) {
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            PreparedStatement customerQuery = connection.prepareStatement(query);
            ResultSet customerInfo = customerQuery.executeQuery();
            while(customerInfo.next()) {
                customers.add(new Customer(
                        customerInfo.getString("First_Name") + " " + customerInfo.getString("Last_Name"),
                        customerInfo.getString("Gender"),
                        customerInfo.getString("Email_ID"),
                        customerInfo.getString("Phone_No"),
                        customerInfo.getString("Username"),
                        customerInfo.getString("User_ID"),
                        customerInfo.getInt("Age")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new MaintainCustomerResponse(customers);
    }
}
